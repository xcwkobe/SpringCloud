package com.xcw.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.connection.RedisConnection;

/**
 * @class: RedisJob
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
public class RedisJob implements Job {

    private RedisConnection connection;

    private long offset;

    private String pointKey;

    private String offsetKey;

    private boolean pause;

    public RedisJob(RedisConnection connection, long offset, String pointKey, String offsetKey) {
        this.connection = connection;
        this.offset = offset;
        this.pointKey = pointKey;
        this.offsetKey = offsetKey;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (!pause){
            //offset：每定时执行一次加一
            connection.set(offsetKey.getBytes(),String.valueOf(offset).getBytes());
            connection.setBit(pointKey.getBytes(),offset,true);
            offset++;
            log.info("定时打点执行中....");
            log.info("offset [{}]", offset);
            log.info("offsetKey [{}]", offsetKey);
            log.info("pointKey [{}]", pointKey);

        }else {

        }
    }
}
