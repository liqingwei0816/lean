package com.guttv.generator.util;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ThymeleafUtil {

    private SpringTemplateEngine springTemplateEngine;

    {
        //2.创建thymeleafTemplate对象
        springTemplateEngine = new SpringTemplateEngine();
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setTemplateMode(TemplateMode.TEXT);

        springTemplateEngine.setTemplateResolver(stringTemplateResolver);
    }

    /**
     * 根据text模板生成
     */
    public String createByTextTemplate(String templateName,Context data) throws URISyntaxException, IOException {
        URI resource = this.getClass().getResource(templateName).toURI();
        String templateString = new String(Files.readAllBytes(Paths.get(resource)), StandardCharsets.UTF_8);
        return springTemplateEngine.process(templateString, data);
    }
    /**
     * 根据text模板生成并写入文件
     */
    public Path createByTextTemplate(String templateName,Context data,String filePath) throws URISyntaxException, IOException {
        Path path = Paths.get(filePath);
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        return Files.write(path, createByTextTemplate(templateName,data).getBytes(StandardCharsets.UTF_8));
    }
    /**
     * 根据text模板生成并写入文件
     */
    public void createBean(String templateName,Context data,String filePath) throws URISyntaxException, IOException {
        createByTextTemplate(templateName,data,filePath);
    }





    /*public void create(String tableSchema, String tableName, String packageName, String beanName, String projectName, boolean controller) throws IOException, URISyntaxException {
        //项目路径
        String fileRoot = "\\templates";//UnicodeUtil.class.getResource("/").getPath().split("generator")[0] + projectName;
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(fileRoot.split("/")).filter(s1 -> !s1.equals("")).forEach(e -> stringBuilder.append(e).append("\\"));
        fileRoot = stringBuilder.toString();

        String base = fileRoot + "src\\main\\java\\" + packageName.replace('.', '\\') + "\\";
        List<TableInfoBean> tableInfo = baseMapper.getTableInfo(tableSchema, tableName);

        //转换大写及mybatis和mysql类型映射
        tableInfo.forEach(e -> {
            if (Objects.equals(e.getDataType(), "int")) {
                e.setDataType("Integer");
            }
        });
        //1.获取数据库表信息
        Map<String, Object> table = new HashMap<>();
        table.put("packageName", packageName);
        table.put("beanName", beanName);
        table.put("tableName", tableName);
        table.put("tableInfo", tableInfo);
        StringBuilder sqlList = new StringBuilder();
        tableInfo.forEach(e -> sqlList.append(e.getColumnName()).append(","));
        table.put("keyList", sqlList.deleteCharAt(sqlList.length() - 1));

        //3.准备数据
        Context context = new Context();
        context.setVariables(table);
        //4.根据表信息生成文件 mapper.xml，beanMapper,service,serviceImpl,controller


        //生成文件Mapper.xml
        String mapperXml = createMapperText("/mapperModel.text", context);
        Path mapperPath = Paths.get(fileRoot + "src\\main\\resources\\mapper\\" + beanName + "Mapper.xml");
        if (!mapperPath.toFile().getParentFile().exists()) {
            mapperPath.toFile().getParentFile().mkdirs();
        }
        Files.write(mapperPath, mapperXml.replace("\r\n\r\n", "").getBytes(StandardCharsets.UTF_8));

        //生成文件Mapper接口
        String mapperInterface = createMapperText("/mapperInterface.text", context);
        Path mapperInterfacePath = Paths.get(base + "/mapper/" + beanName + "Mapper.java");
        if (!mapperInterfacePath.toFile().getParentFile().exists()) {
            mapperInterfacePath.toFile().getParentFile().mkdirs();
        }

        Files.write(mapperInterfacePath, mapperInterface.getBytes(StandardCharsets.UTF_8));

        //生成文件service接口
        String service = createMapperText("/service.text", context);
        Path servicePath = Paths.get(base + "/service/" + beanName + "Service.java");
        if (!servicePath.toFile().getParentFile().exists()) {
            servicePath.toFile().getParentFile().mkdirs();
        }

        Files.write(servicePath, service.getBytes(StandardCharsets.UTF_8));


        //生成文件serviceImpl
        String serviceImpl = createMapperText("/serviceImpl.text", context);
        Path serviceImplPath = Paths.get(base + "/service/impl/" + beanName + "ServiceImpl.java");
        if (!serviceImplPath.toFile().getParentFile().exists()) {
            serviceImplPath.toFile().getParentFile().mkdirs();
        }
        Files.write(serviceImplPath, serviceImpl.getBytes(StandardCharsets.UTF_8));


        if (controller) {
            //生成文件controller
            String controllerStr = createMapperText("/controller.text", context);
            // String controllerPath = base.replaceAll("mdn-common", "mdn-console").replace("common", "console");
            Path controllerPathObj = Paths.get(fileRoot + "\\controller\\" + beanName + "Controller.java");
            if (!controllerPathObj.toFile().getParentFile().exists()) {
                controllerPathObj.toFile().getParentFile().mkdirs();
            }
            Files.write(controllerPathObj, controllerStr.getBytes(StandardCharsets.UTF_8));
        }

        //生成文件bean.java
        createBean(table);
        String beanXml = createMapperText("/bean.text", context);
        Path beanPath = Paths.get(base + "/bean/" + beanName + ".java");
        if (!beanPath.toFile().getParentFile().exists()) {
            beanPath.toFile().getParentFile().mkdirs();
        }

        Files.write(beanPath, beanXml.getBytes());
    }*/




}
