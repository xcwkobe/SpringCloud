package com.xcw.controller;

import com.xcw.entity.Meeting;
import com.xcw.entity.MeetingParticipant;
import com.xcw.event.UpdateJoinTimeEvent;
import com.xcw.quartz.RedisTask;
import com.xcw.service.MeetingService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * @class: DataController
 * @author: ChengweiXing
 * @description: TODO
 **/
@RestController
@Slf4j
public class DataController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultRedisScript script;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Timer timer = new Timer();

    private RedisTask redisTask;

    private static final Long TIME_INTERVAL = 5000L;

    @ApiOperation("设置会议开始时间")
    @GetMapping("/meetingTime/{meetingId}")
    public String setTime(@PathVariable("meetingId") Long meetingId) {
        setStartTime(meetingId, Instant.now().toEpochMilli());
        return "success";
    }

    @ApiOperation("上报数据埋点")
    @GetMapping("/data/report/{meetingId}/{accountId}")
    public void dataPoint(@PathVariable("meetingId") Long meetingId, @PathVariable("accountId") Long accountId) {
        try {

            long startTime = getStartTime(meetingId);
            long now = Instant.now().toEpochMilli();
            if (now > startTime) {
                Long offset = (now - startTime) / TIME_INTERVAL;
                String pointKey = getMeetingDataKey(meetingId, accountId);
                String offsetKey = getLastOffsetKey(meetingId, accountId);
                redisTemplate.opsForValue().set(offsetKey, String.valueOf(offset));
                redisTemplate.opsForValue().setBit(pointKey, offset, true);
                log.info("offset [{}]", offset);
                log.info("offsetKey [{}]", offsetKey);
                log.info("pointKey [{}]", pointKey);
                //bitmap中1第一次出现的位置
                Long startOffset = (Long) redisTemplate.execute((RedisCallback<Long>) con ->
                        con.bitPos(pointKey.getBytes(), true));
                //如果当前的偏移量等于1第一次出现的位置，就是参会时间
                if (offset.equals(startOffset)) {
                    MeetingParticipant temp = new MeetingParticipant();
                    temp.setAccountId(accountId);
                    temp.setMeetingId(meetingId);
                    //参会的时间
                    temp.setJoinTime(new Date(startTime + startOffset * TIME_INTERVAL));
                    //spring发布事件，异步处理
                    eventPublisher.publishEvent(new UpdateJoinTimeEvent(temp));
                }

                //long testEnd = System.currentTimeMillis();
                //log.debug("不使用pipeline消耗时长：{}秒",(testEnd-testStart)/1000L);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @GetMapping("/data/pipeline/{meetingId}/{accountId}")
    public void dataPoint2(@PathVariable("meetingId") Long meetingId, @PathVariable("accountId") Long accountId,
                           boolean joinMeeting) {
        if (joinMeeting) {
            long startTime = getStartTime(meetingId);
            long now = Instant.now().toEpochMilli();
            if (now > startTime) {
                Long offset = (now - startTime) / TIME_INTERVAL;
                String pointKey = getMeetingDataKey(meetingId, accountId);
                String offsetKey = getLastOffsetKey(meetingId, accountId);
                //可以先判断是不是参会的时间，如果不是，会返回-1
                Long startOffset = (Long) redisTemplate.execute((RedisCallback<Long>) con ->
                        con.bitPos(pointKey.getBytes(), true));
                if (startOffset == -1) {
                    //设置bitmap初始值
                    redisTemplate.opsForValue().setBit(pointKey, (getEndTime(meetingId) - startTime) / TIME_INTERVAL, false);
                }
                //如果当前的偏移量等于1第一次出现的位置，就是第一次参会时间
                if (offset.equals(startOffset)) {
                    MeetingParticipant temp = new MeetingParticipant();
                    temp.setAccountId(accountId);
                    temp.setMeetingId(meetingId);
                    //参会的时间
                    temp.setJoinTime(new Date(startTime + startOffset * TIME_INTERVAL));
                    //spring发布事件，异步处理
                    eventPublisher.publishEvent(new UpdateJoinTimeEvent(temp));
                }

                //方案一：用pipeline,执行定时任务打点
                redisTemplate.executePipelined((RedisCallback<?>) connection -> {
                    connection.openPipeline();
                    redisTask = new RedisTask(connection, pointKey, offset, offsetKey);
                    redisTask.setPause(false);
                    timer.schedule(redisTask, 0, TIME_INTERVAL);
                    return null;
                });

                //方案二：lua脚本打点,把两个key传给lua
                String startTimeKey = getStartTimeKey(meetingId);
                List<String> keys = Arrays.asList(startTimeKey, pointKey, offsetKey);
                redisTemplate.execute(script, keys, String.valueOf(offset));

            }
        } else {//参会人离开会议,停止打点
            redisTask.setPause(true);
        }
    }

    private long getEndTime(Long meetingId) {
        return 0;
    }

    private String getLastOffsetKey(Long meetingId, Long accountId) {
        return "meeting:" + meetingId + ":" + accountId + ":offset";
    }

    private String getMeetingDataKey(Long meetingId, Long accountId) {
        return "meeting:" + meetingId + ":" + accountId;
    }

    /**
     * redis获取会议开始时间
     *
     * @param meetingId
     * @return
     */
    private long getStartTime(Long meetingId) {
        String startTimeKey = getStartTimeKey(meetingId);
        String value = (String) redisTemplate.opsForValue().get(startTimeKey);
        //如果开始时间为空，设置为真实的开始时间
        if (StringUtils.isBlank(value)) {
            Meeting meeting = meetingService.getById(meetingId);
            long startTime = meeting.getRealStartTime().getTime();
            setStartTime(meetingId, startTime);
            return startTime;
        }
        return Long.valueOf(value);
    }

    /**
     * redis设置会议开始时间
     *
     * @param meetingId
     * @param startTime
     */
    private void setStartTime(Long meetingId, long startTime) {
        String startTimeKey = getStartTimeKey(meetingId);
        redisTemplate.opsForValue().set(startTimeKey, String.valueOf(startTime));
    }

    private String getStartTimeKey(Long meetingId) {
        return "meeting:" + meetingId + ":start";
    }
}
