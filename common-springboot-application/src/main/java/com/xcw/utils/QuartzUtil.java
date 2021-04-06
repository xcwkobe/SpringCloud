package com.xcw.utils;

import com.xcw.constant.QuartzConst;
import com.xcw.entity.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @class: QuartzUtil
 * @author: ChengweiXing
 * @description: TODO
 **/
@Component
@Slf4j
public class QuartzUtil {

    @Autowired
    private Scheduler scheduler;

    /**
     * 添加一个定时任务
     * @param quartzModel
     */
    public void addJob(QuartzJob quartzModel) {
        if (quartzModel.verify()) {
            try {
                JobDetail job = JobBuilder.newJob(quartzModel.getJobClass())
                        .withIdentity(quartzModel.getJobName(), quartzModel.getJobGroupName())
                        .setJobData(quartzModel.getJobDataMap()).build();
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(quartzModel.getTriggerName(), quartzModel.getTriggerGroupName())
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(quartzModel.getSeconds()))
                        .build();
                scheduler.scheduleJob(job, trigger);
                // 启动
                if (!scheduler.isShutdown()) {
                    scheduler.start();
                }
            }
            catch (SchedulerException e) {
                log.error("Add quartz job error, jobName = {}", quartzModel.getJobName());
            }

        }
        else {
            log.error("QuartzModel is invalid!");
        }
    }

    /**
     * @Description:暂停一个任务
     * @param jobName
     * @param jobGroupName
     */
    public void pauseJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.pauseJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:恢复一个任务
     * @param jobName
     * @param jobGroupName
     */
    public void resumeJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.resumeJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public void startJobs() {
        try {
            scheduler.start();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description 关闭所有定时任务
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 立即运行任务，这里的立即运行，只会运行一次，方便测试时用。
     * @param jobName
     */
    public void triggerJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, QuartzConst.QZ_JOB_GROUP_NAME);
        try {
            scheduler.triggerJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 立即运行任务，这里的立即运行，只会运行一次，方便测试时用。
     * @param jobName
     * @param jobGroupName
     */
    public void triggerJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.triggerJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 获取任务状态
     * @param jobName
     *            触发器名
     */
    public String getTriggerState(String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, QuartzConst.QZ_TRIGGER_GROUP_NAME);
        String name = null;
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            name = triggerState.name();
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * @Description:获取最近5次执行时间
     * @param cron
     */
    public List<String> getRecentTriggerTime(String cron) {
        List<String> list = new ArrayList<String>();
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 5);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            for (Date date : dates) {
                list.add(dateFormat.format(date));
            }
        }
        catch (ParseException e) {
            log.error("GetRecentTriggerTime error, cron = {}", cron, e);
        }
        return list;
    }


}
