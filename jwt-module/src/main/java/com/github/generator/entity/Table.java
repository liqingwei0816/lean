package com.github.generator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.generator.GeneratorProperty;
import com.github.generator.mapper.TableMapper;
import com.github.pagehelper.IPage;

import com.github.util.SpringUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Table implements IPage {

    private String name;
    private String entityName;
    private String beanName;
    private String comment;
    //属性名
    private String keyList;
    private String namePrefix;
    List<Field> fields;
    //分页
    private String orderBy;
    @JsonIgnore
    private Integer pageSize = 20;
    @JsonIgnore
    private Integer pageNum = 1;

    public List<Field> getFields() {
        TableMapper tableMapper = SpringUtil.getBean(TableMapper.class);
        return tableMapper.getFields(name);
    }

    public String getBeanName() {
        String entityName = getEntityName();
        return entityName.replaceFirst(String.valueOf(entityName.charAt(0)), String.valueOf(entityName.charAt(0)).toLowerCase());
    }

    public String getKeyList() {
        return getFields().stream().map(Field::getName).collect(Collectors.joining(","));
    }

    @SuppressWarnings("unused")
    public String getNamePrefix() {
        GeneratorProperty generatorProperty = SpringUtil.getBean(GeneratorProperty.class);
        return generatorProperty.getTablePrefix();
    }

    @SuppressWarnings("unused")
    public String getEntityName() {
        //beanName
        //去除表名前缀
        GeneratorProperty generatorProperty = SpringUtil.getBean(GeneratorProperty.class);
        String beanName = name.replaceFirst(generatorProperty.getTablePrefix(), "");
        //转驼峰
        beanName = Stream.of(beanName.split("_")).map(e -> {
            String s = String.valueOf(e.charAt(0));
            return e.replaceFirst(s, s.toUpperCase());
        }).collect(Collectors.joining());
        return beanName;
    }

    public static List<Table> parse(List<Map<String, Object>> list) {
        return list.stream().map(e -> {
            Table table = new Table();
            table.setName(e.values().iterator().next().toString());
            return table;
        }).collect(Collectors.toList());

    }
}
