package com.xcw.controller;

import com.xcw.constant.QuartzConst;
import com.xcw.entity.QuartzJob;
import com.xcw.entity.User;
import com.xcw.quartz.RedisJob;
import com.xcw.service.UserService;
import com.xcw.utils.QuartzUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Timer;

/**
 * @class: TestController
 * @author: ChengweiXing
 * @description: TODO
 **/
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuartzUtil quartzUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    private Timer timer = new Timer();

    @GetMapping("/test")
    public String test(String para){
        return para;
    }

    @ApiOperation("根据id获得user")
    @GetMapping("/getuser/{id}")
    public User getUser(@PathVariable("id") int id){
        User byId = userService.getById(id);
        return byId;
    }

    @GetMapping("/addJob")
    public String addJob(){
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setJobClass(RedisJob.class);
        quartzJob.setJobGroupName(QuartzConst.QZ_JOB_GROUP_NAME);
        quartzJob.setTriggerGroupName(QuartzConst.QZ_TRIGGER_GROUP_NAME);
        quartzJob.setStartTime(new Date());
        quartzJob.setSeconds(5);
        quartzJob.setJobName("test");
        quartzJob.setTriggerName("testtrigger");
        //quartzJob.setEndTime(new Date());
        quartzUtil.addJob(quartzJob);
        return "success";
    }

    @GetMapping("/pauseJob")
    public String pause(){
        quartzUtil.pauseJob("test","testGroup");
        String res = quartzUtil.getTriggerState("test");
        return res;
    }

    @GetMapping("/resumeJob")
    public String resume(){
        quartzUtil.resumeJob("test","testGroup");
        return quartzUtil.getTriggerState("test");
    }

//    @GetMapping("/startJob")
//    public String start(){
//
//        redisTemplate.executePipelined((RedisCallback<List<Long>>) con -> {
//            con.openPipeline();
//            RedisTask redisTask = new RedisTask(con,"",10,"");
//            timer.scheduleAtFixedRate(redisTask,0,5000);
//            return null;
//        });
//
//        return "success";
//    }
//
//    @GetMapping("/cancelJob")
//    public String cancel(){
//        timer.cancel();
//        return "canceled";
//    }
}
