package com.github.job;

import com.github.bean.system.Auth;
import com.github.service.system.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@Slf4j
@SuppressWarnings("unused")
public class SampleJob1 extends QuartzJobBean {
    /**
     * 直接注入ioc中的bean
     */
    @Resource
    private AuthService authService;


    private String name;
    public void setName(String name) {

    }
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long count = authService.count(new Auth());
        log.error("{} job1 {}",count,context.getScheduledFireTime());
    }
}
