package com.cloud;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.cloud.service.SayHelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class Web {

    @Resource
    private SayHelloService sayHelloService;
    @RequestMapping("/")
    public String test(String name){
        List<DegradeRule> rules = DegradeRuleManager.getRules();
        rules.forEach(e-> System.out.println(JSON.toJSONString(e)));
        List<FlowRule> flowRules = FlowRuleManager.getRules();
        flowRules.forEach(e-> System.out.println(JSON.toJSONString(e)));
        return sayHelloService.sayHello(name);
    }
    @RequestMapping("/sayBey")
    public String sayBey(){
        return sayHelloService.sayBey();
    }


}
