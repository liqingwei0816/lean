package com.github.job;

import com.github.bean.system.Auth;
import com.github.service.system.AuthService;
import javax.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SampleJob1 extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(SampleJob1.class);
    @Resource
    private AuthService authService;
    private String name;

    public SampleJob1() {
    }

    public void setName(String name) {
    }

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long count = this.authService.count(new Auth());
        log.error("{} job1 {}", count, context.getScheduledFireTime());
    }
}
