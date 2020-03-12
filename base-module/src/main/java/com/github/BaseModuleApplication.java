package com.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class BaseModuleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(BaseModuleApplication.class, args);
        run.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach((k, v) -> System.out.println(k.getPatternsCondition()));
    }

}
