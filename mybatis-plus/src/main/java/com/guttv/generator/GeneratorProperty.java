package com.guttv.generator;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class GeneratorProperty {
    @Value("${generator.package:com.guttv}")
    private String packageName;
    @Value("${generator.table.prefix:t_}")
    private String tablePrefix;
    @Value("${spring.datasource.url}")
    private String databaseName;

    public String getDatabaseName() {
        return  databaseName.substring(databaseName.lastIndexOf("/")+1,databaseName.indexOf("?"));
    }


}
