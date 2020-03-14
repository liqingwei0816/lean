package com.github.job;

import com.github.bean.system.Auth;
import com.github.service.system.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@Slf4j
/*
 * 不允许并发执行注解
 * 存在，则允许一个QuartzJob并发执行，否则就是顺序执行
 * 例如QuartzJob A执行时间为15秒，配置为每10秒执行一次；
 * 如果此注解不存在，则0秒的时候启动一次A，10秒的时候再启动一次A，20秒的时候再启动一次A，不管前面启动的A有没有执行完；
 * 如果此注解存在，则0秒的时候启动一次A，15秒的时候A执行完毕，再第二次启动A 但对于总的执行次数两者是相同的，只是执行的时间不同。
 */
//@DisallowConcurrentExecution
@SuppressWarnings("unused")
public class SampleJob extends QuartzJobBean {
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
        log.error("{} job {}",count,context.getScheduledFireTime());
    }
}
