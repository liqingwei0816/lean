/*
package com.guttv.config;

import com.guttv.job.SampleJob;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Resource
    private Scheduler scheduler;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String jobName="testJob";
        String jobGroup="testGroup";

        JobDetail jobDetail = JobBuilder.newJob(SampleJob.class).withIdentity("testJob","testGroup").build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
//						cronTrigger.getJobDataMap().put("jobEntity", myJob);

        scheduler.scheduleJob(jobDetail, cronTrigger);
        //启动一个定时器
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }
}
*/
