package com.guttv;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {
        String databaseName = "jdbc:mysql://localhost:3306/guttv_ums?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2b8";
        databaseName= databaseName.substring(databaseName.lastIndexOf("/")+1,databaseName.indexOf("?"));



        /*String path = "/D:/IdeaProjects/lean/mybatis-plus/target/classes/com/guttv/generator/util/";
        path = path.substring(0, path.indexOf("target"));
        String packageName = "com.guttv";
        path = path+packageName.replace(".", File.separator);
*/


        /*String beanName = "t_user_info".replaceFirst("t_", "");
        //转驼峰
        beanName = Stream.of(beanName.split("_")).map(e -> {
            String s = String.valueOf(e.charAt(0));
            return e.replaceFirst(s, s.toUpperCase());
        }).collect(Collectors.joining());*/
        System.out.println(databaseName);
    }

}
