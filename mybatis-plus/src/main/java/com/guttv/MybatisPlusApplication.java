package com.guttv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class MybatisPlusApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(MybatisPlusApplication.class, args);
        RequestMappingHandlerMapping mapping = run.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        Set<RequestMappingInfo> requestMappingInfos = handlerMethods.keySet();
        requestMappingInfos.forEach(e->{
            Set<String> patterns = e.getPatternsCondition().getPatterns();
            patterns.forEach(System.out::println);
        });

    }

}
