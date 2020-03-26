package com.github.quartz.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.github.util.SpringUtil;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.util.StringUtils;

/**
 * job日志过滤器 适配于logback
 */
public class JobLogFilter extends Filter<ILoggingEvent> {

    private String threadNamePrefix;

    @Override
    public FilterReply decide(ILoggingEvent event) {

        if (event.getLoggerName().startsWith("org.quartz")){
            return FilterReply.DENY;
        }


        //获取工作当前线程名
        String threadName = event.getThreadName();
        if (StringUtils.isEmpty(threadNamePrefix)) {
            QuartzProperties bean = SpringUtil.getBean(QuartzProperties.class);
            threadNamePrefix = bean.getProperties().get("org.quartz.threadPool.threadNamePrefix");
        }


        if (threadName.startsWith(threadNamePrefix)) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }
}
