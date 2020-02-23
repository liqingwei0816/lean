package com.guttv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@SpringBootApplication
public class BaseModuleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(BaseModuleApplication.class, args);
        RequestMappingHandlerMapping bean = run.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            System.out.println(k.getPatternsCondition());
        });
    }

}
