package com.guttv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class Head implements Runnable {

    static Logger logger = LoggerFactory.getLogger(Head.class);
    @Override
    public void run() {
        MDC.put("logFileName","head-"+Thread.currentThread().getName());
        for (int j = 0; j < 10; j++) {
            logger.info("我是线程输出日志："+Thread.currentThread().getName());
        }
        MDC.clear();
    }
}
