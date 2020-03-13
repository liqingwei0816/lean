package com.github.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Resource
    private CacheManager cacheManager;
    @Override
    public void run(ApplicationArguments args) throws Exception {
       System.out.println(cacheManager.getClass().getName());

    }
}

