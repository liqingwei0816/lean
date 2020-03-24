package com.github.quartz;

import com.github.quartz.mapper.DynamicJobService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 指定数据源
 */
@Configuration
public class QuartzConfig {

    /**
     * 用于quartz使用独立数据源时为动态修改job相关表切换数据源
     * @param dataSource 默认数据源
     * @param quartzDataSource  quartz数据源
     */
    @Bean
    public DynamicJobService dynamicJobService(DataSource dataSource,@QuartzDataSource ObjectProvider<DataSource> quartzDataSource) {
        DataSource ifAvailable = quartzDataSource.getIfAvailable();
        DataSource quartzDataSource1 = ifAvailable == null ? dataSource : ifAvailable;
        return new DynamicJobService(quartzDataSource1);
    }


}
