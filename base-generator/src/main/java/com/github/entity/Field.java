package com.github.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Slf4j
public class Field {
    private String type;
    private String name;
    private String javaType;
    private Integer length;
    private String comment;
    private boolean view;
    private static Map<String, String> typeMapping;

    @SuppressWarnings("unused")
    public String getJavaType() {
        return sql2Java();
    }

    private String sql2Java() {
        String typeUp = type.toUpperCase();
        if ((Objects.equals(typeUp, "BIT") || Objects.equals(typeUp, "TINYINT")) && Objects.equals(length,1)) {
            return "Boolean";
        }

        String orDefault = typeMapping.getOrDefault(typeUp, "");
        if (orDefault.equals("")) {
            log.error("未获取到正确的类型映射信息[{}]", typeUp);
        }



        return orDefault;

    }

    static {
        typeMapping = new HashMap<>();
        typeMapping.put("BIT", "byte[]");
        typeMapping.put("TINYINT", "Integer");
        typeMapping.put("BOOL", "Integer");
        typeMapping.put("BOOLEAN", "Integer");
        typeMapping.put("SMALLINT", "Integer");
        typeMapping.put("MEDIUMINT", "Integer");
        typeMapping.put("INT", "Integer");
        typeMapping.put("INTEGER", "Integer");
        typeMapping.put("BIGINT", "Long");
        typeMapping.put("FLOAT", "Float");
        typeMapping.put("DOUBLE", "Double");
        typeMapping.put("DECIMAL", "java.math.BigDecimal");
        typeMapping.put("DATE", "java.sql.Date");
        typeMapping.put("DATETIME", "java.time.LocalDateTime");
        typeMapping.put("TIMESTAMP", "java.time.LocalDateTime");
        typeMapping.put("TIME", "java.time.LocalTime");
        typeMapping.put("CHAR", "String");
        typeMapping.put("VARCHAR", "String");
        typeMapping.put("BINARY", "byte[]");
        typeMapping.put("VARBINARY", "byte[]");
        typeMapping.put("TINYBLOB", "byte[]");
        typeMapping.put("TINYTEXT", "String");
        typeMapping.put("BLOB", "byte[]");
        typeMapping.put("TEXT", "String");
        typeMapping.put("MEDIUMBLOB", "byte[]");
        typeMapping.put("MEDIUMTEXT", "String");
        typeMapping.put("LONGBLOB", "byte[]");
        typeMapping.put("LONGTEXT", "String");
        typeMapping.put("ENUM", "String");
        typeMapping.put("SET", "String");
    }


}
