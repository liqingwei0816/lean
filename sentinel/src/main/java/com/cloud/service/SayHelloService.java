package com.cloud.service;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SayHelloService {


    @SentinelResource(value = "sayHello",
            blockHandler = "handleException")
    public String sayHello(String name) {
        int i = new Random().nextInt(10);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello "+name;
    }

    /**
     *
     * @param name 必须与原方法参数列表一致
     * @param ex 必须使用BlockException类型
     * @return 必须与原返回类型一致
     */
    public String handleException(String name, BlockException ex){

        //do some log here

        ex.printStackTrace();
        return "Oops, error occurred at " + name;
    }
    public String helloFallback(String name,BlockException ex){
        return "helloFallback  " + name;
    }



}
