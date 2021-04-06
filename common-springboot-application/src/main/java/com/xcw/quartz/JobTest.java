package com.xcw.quartz;

import com.xcw.controller.DataController;
import com.xcw.utils.QuartzUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @class: JobTest
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
@Component
public class JobTest implements Job {

    @Autowired
    private DataController dataController;

    @Autowired
    private QuartzUtil quartzUtil;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println("执行定时任务中。。");
    }

    public static void main(String[] args) throws SchedulerException, InterruptedException {

    }
}
