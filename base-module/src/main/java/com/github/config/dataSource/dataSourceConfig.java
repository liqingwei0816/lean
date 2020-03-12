package com.github.config.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * quartz独立数据源配置 springboot 多数据源
 */
//@Configuration
//@ConditionalOnClass({Scheduler.class, DruidDataSource.class})
public class dataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource baseDataSource() {
        return new DruidDataSource();
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
        return new DruidDataSource();
    }

}
