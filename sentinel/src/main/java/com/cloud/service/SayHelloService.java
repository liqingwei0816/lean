package com.cloud.service;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContextListener;
import java.util.Random;

@Service
public class SayHelloService implements ServletContextListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * value 资源名
     * handleException 降级时处理逻辑
     * helloFallback 异常时处理逻辑
     * 若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑
     */
    @SentinelResource(value = "sayHello",
            blockHandler = "handleException", fallback = "helloFallback")
    public String sayHello(String name) {
        int i = new Random().nextInt(10);
        try {
            Thread.sleep(500);
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       /* if (i < 5) {
            throw new RuntimeException("RuntimeException");
        }*/
        return "hello " + name;
    }

    /**
     * @param name 必须与原方法参数列表一致
     * @param ex   必须使用BlockException类型
     * @return 必须与原返回类型一致
     */
    public String handleException(String name, BlockException ex) {

        //do some log here
        log.warn("降级处理");
        return "降级处理 " + name;
    }

    public String helloFallback(String name, Throwable ex) {
        log.warn("异常处理");
        return "异常处理  " + ex.getMessage();
    }

    /**
     * value 资源名
     * handleException 降级时处理逻辑
     * helloFallback 异常时处理逻辑
     * 若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑
     */
    @SentinelResource(value = "sayBey")
    public String sayBey() {
        return "sayBey";
    }
}
