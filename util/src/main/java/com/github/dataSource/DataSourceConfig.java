package com.github.dataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * quartz独立数据源配置 springboot 多数据源动态切换
 */
@Configuration
public class DataSourceConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource baseDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     *  quartz:
     *       url: jdbc:mysql://localhost:3306/quartz?zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true
     *       username: root
     *       password: 123456
     */
    @Bean
    @QuartzDataSource
    @ConfigurationProperties("spring.datasource.quartz")
    public DataSource quartzDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Primary
    @Bean
    public DataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        Map<Object, Object> dataSources = new ConcurrentHashMap<>();
        dataSources.put("QuartzDataSource",quartzDataSource());
        dynamicDataSource.setTargetDataSources(dataSources);

        dynamicDataSource.setDefaultTargetDataSource(baseDataSource());
        return dynamicDataSource;
    }

}
