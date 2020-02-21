package com.guttv.generator;

import com.guttv.util.SpringUtil;
import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "spring.generator")
public class GeneratorProperty {

    /**
     * 项目基础包名
     */
    private String packageName ="com.guttv";

    /**
     * 表名前缀
     */
    private String tablePrefix ="t_";
    /**
     * 多模块项目时，必须指定项目名
     */
    private String projectName ="";

    public String getDatabaseName() {
        DataSourceProperties dataSourceProperties = SpringUtil.getBean(DataSourceProperties.class);
        String databaseName=dataSourceProperties.getUrl();
        return  databaseName.substring(databaseName.lastIndexOf("/")+1,databaseName.indexOf("?"));
    }



}
