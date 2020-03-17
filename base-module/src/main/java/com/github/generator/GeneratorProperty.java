package com.github.generator;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


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
     * 表名前缀 todo 统一为第一个_前的为前缀
     */
    private String tablePrefix ="t_";

    /**
     * 数据库名称
     */
    private String databaseName;





}
