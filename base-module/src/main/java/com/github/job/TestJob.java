package com.github.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@DisallowConcurrentExecution
public class TestJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Date fireTime = context.getFireTime();
        Object data = context.getTrigger().getJobDataMap().get("data");
        System.out.println(fireTime+"-修改之后--"+data);


    }
}
