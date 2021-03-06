package com.github;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Data
@Component
@ConfigurationProperties(prefix = "spring.generator")
@Slf4j
public class GeneratorProperty {
    /**
     * 项目基础包名
     */
    private String packageName ="com.github";

    /**
     * 数据库名称
     */
    private String databaseName;

    @Resource
    private DataSourceProperties dataSourceProperties;

    @Resource
    private Environment environment;

    public String getDatabaseName(){
        String property = dataSourceProperties.getUrl();
       // String property = environment.getProperty("spring.datasource");
        if (property==null){
            throw new RuntimeException("spring.datasource.url 配置不存在");
        }
        return property.substring(property.lastIndexOf("/")+1,property.indexOf("?"));
    }




}
