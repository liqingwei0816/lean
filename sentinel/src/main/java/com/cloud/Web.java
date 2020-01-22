package com.cloud;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
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
        System.out.println("SystemRuleManager");
        SystemRuleManager.getRules().forEach(e-> System.out.println(JSON.toJSONString(e)));
        System.out.println("DegradeRuleManager");
        DegradeRuleManager.getRules().forEach(e-> System.out.println(JSON.toJSONString(e)));
        System.out.println("FlowRuleManager");
        FlowRuleManager.getRules().forEach(e-> System.out.println(JSON.toJSONString(e)));
        return sayHelloService.sayHello(name);
    }
    @RequestMapping("/sayBey")
    public String sayBey(){
        return sayHelloService.sayBey();
    }


}
