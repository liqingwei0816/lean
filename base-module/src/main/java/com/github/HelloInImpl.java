package com.github;

import com.github.service.system.AuthService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;

public class HelloInImpl extends QuartzJobBean {

    @Resource
    private AuthService authService;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Date fireTime = context.getFireTime();
        Object data = context.getTrigger().getJobDataMap().get("data");
        System.out.println(fireTime+"-1修1改1之1后1--"+data);
        int size = authService.getAll().size();
        System.out.println("-size--"+size);
    }
}
