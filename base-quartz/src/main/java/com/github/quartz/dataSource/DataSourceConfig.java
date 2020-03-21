package com.github.quartz.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * quartz独立数据源配置 springboot 多数据源
 */
@Configuration
@ConditionalOnClass({Scheduler.class, DruidDataSource.class})
public class DataSourceConfig {

    @Primary
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

}
