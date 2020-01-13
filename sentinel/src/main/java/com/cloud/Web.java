package com.cloud;

import com.cloud.service.SayHelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Web {

    @Resource
    private SayHelloService sayHelloService;
    @RequestMapping("/")
    public String test(String name){
        return sayHelloService.sayHello(name);
    }


}
