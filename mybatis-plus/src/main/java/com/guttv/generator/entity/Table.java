package com.guttv.generator.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Table {

    private String name;
    private String entityName;
    private String comment;
    private String namePrefix = "t_";
    List<Field> fields;


    @SuppressWarnings("unused")
    public String getEntityName() {
        StringBuilder nameVo;
        //表单前缀
        if (StringUtils.isEmpty(namePrefix)) {
            nameVo =new StringBuilder(name);
        } else {
            nameVo = new StringBuilder(name.replaceFirst(namePrefix, ""));
        }

        //改驼峰
        int i = nameVo.lastIndexOf("_");
        while (i != -1) {
            nameVo.deleteCharAt(i);
            String c = nameVo.charAt(i) + "";
            nameVo.setCharAt(i, c.toUpperCase().charAt(0));
            i = nameVo.lastIndexOf("_");
        }
        String c = nameVo.charAt(0) + "";
        nameVo.setCharAt(0, c.toUpperCase().charAt(0));
        return nameVo.toString();


    }

    public static List<Table> parse(List<Map<String, Object>> list) {
        return list.stream().map(e -> {
            Table table = new Table();
            table.setName(e.values().iterator().next().toString());
            return table;
        }).collect(Collectors.toList());

    }

}
