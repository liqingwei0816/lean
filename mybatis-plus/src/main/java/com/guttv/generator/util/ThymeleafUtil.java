package com.guttv.generator.util;

import com.guttv.generator.GeneratorProperty;
import com.guttv.generator.entity.Table;
import com.guttv.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
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
    public String createByTextTemplate(String templateName, Context data) throws IOException {
        Resource resource = new ClassPathResource(templateName);
        if (!resource.exists()) {
            log.error("文件不存在：{}", resource.getURI().getPath());
            return null;
        } else {

            byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
            String templateString = new String(bytes, StandardCharsets.UTF_8);
            return springTemplateEngine.process(templateString, data);
        }

    }

    /**
     * 根据text模板生成并写入文件
     */
    public Path createByTextTemplate(String templateName, Context data, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            boolean mkdir = file.getParentFile().mkdirs();
            if (!mkdir){
                throw new RuntimeException("创建文件夹失败:"+file.getParentFile().getPath());
            }
        }
        return Files.write(file.toPath(), createByTextTemplate(templateName, data).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据text模板生成并写入文件
     */
    public void createBean(Table table) throws URISyntaxException, IOException {
        Context context = getContext(table);
        String path = getObjectPath();
        String packageName = context.getVariable("packageName").toString();
        path = path + "src\\main\\java\\" + packageName.replace(".", File.separator) + File.separator + "bean" + File.separator + table.getEntityName() + ".java";
        Path byTextTemplate = createByTextTemplate("templates/generator/bean.java", context, path);
        log.info("生成实体类文件，位置为：{}", byTextTemplate.toUri().getPath());
    }

    /**
     * 根据text模板生成Mapper相关并写入文件
     */
    public void createMapper(Table table) throws URISyntaxException, IOException {
        {
            //mapper xml
            String path = getObjectPath();
            path = path + "src\\main\\resources\\mapper" + File.separator + table.getEntityName() + "Mapper.xml";
            Path byTextTemplate = createByTextTemplate("templates/generator/mapper.xml", getContext(table), path);
            log.info("生成MapperXml文件，位置为：{}", byTextTemplate.toUri().getPath());
        }

        {
            //mapper java
            String path = getClassPath(table);
            path = path + "mapper" + File.separator + table.getEntityName() + "Mapper.java";
            Path byTextTemplate = createByTextTemplate("templates/generator/mapper.java", getContext(table), path);
            log.info("生成mapper类文件，位置为：{}", byTextTemplate.toUri().getPath());
        }
    }

    /**
     * 根据text模板生成service相关并写入文件
     */
    public void createService(Table table) throws URISyntaxException, IOException {
        String pathBash = getClassPath(table);
        {
            //service
            String path = pathBash + "service" + File.separator + table.getEntityName() + "Service.java";
            Path byTextTemplate = createByTextTemplate("templates/generator/service.java", getContext(table), path);
            log.info("生成service接口文件，位置为：{}", byTextTemplate.toUri().getPath());
        }

        {
            //service impl
            String path = pathBash + "service" + File.separator + "impl" + File.separator + table.getEntityName() + "ServiceImpl.java";
            Path byTextTemplate = createByTextTemplate("templates/generator/serviceImpl.java", getContext(table), path);
            log.info("生成serviceImpl类文件，位置为：{}", byTextTemplate.toUri().getPath());
        }
    }

    /**
     * 根据text模板生成controller相关并写入文件
     */
    public void createController(Table table) throws URISyntaxException, IOException {

        String pathBash = getClassPath(table);

        //controller
        String path = pathBash + "controller" + File.separator + table.getEntityName() + "Controller.java";
        Path byTextTemplate = createByTextTemplate("templates/generator/controller.java", getContext(table), path);
        log.info("生成controller类文件，位置为：{}", byTextTemplate.toUri().getPath());


    }
    /**
     * 根据text模板生成controller相关并写入文件
     * @param  apiName  Controller的名称是否添加Api后缀 添加后为table.getEntityName() + "ControllerApi.java"
     */
    public void createApi(Table table,boolean apiName) throws URISyntaxException, IOException {
        String pathBash = getClassPath(table);
        String controllerName;
        if (apiName){
            controllerName =table.getEntityName() + "ControllerApi.java";
        }else {
            controllerName =table.getEntityName() + "Controller.java";
        }
        String path = pathBash + "controller" + File.separator + controllerName;
        Path byTextTemplate = createByTextTemplate("templates/generator/controllerApi.java", getContext(table), path);
        log.info("生成controllerApi类文件，位置为：{}", byTextTemplate.toUri().getPath());


    }

    /**
     * 根据text模板生成页面文件相关并写入文件
     */
    public void createHtml(Table table) throws URISyntaxException, IOException {
        //list
        String path = getObjectPath();
        path = path + "src\\main\\resources\\templates" + File.separator + table.getBeanName() + File.separator + "list.html";
        Path byTextTemplate = createByTextTemplate("templates/generator/html.html", getContext(table), path);
        log.info("生成html.html文件，位置为：{}", byTextTemplate.toUri().getPath());
    }

    /**
     * 生成 service mapper bean
     */
    public void createServiceAndLowerLevel(Table table) throws IOException, URISyntaxException {
        createBean(table);
        createMapper(table);
        createService(table);
    }


    private String getObjectPath() throws URISyntaxException {
        String path = this.getClass().getResource(".").toURI().getPath();
        return path.substring(0, path.indexOf("target"));
    }

    /**
     * 不包含service等层的路径
     */
    private String getClassPath(Table table) throws URISyntaxException {
        String path = getObjectPath();
        Context context = getContext(table);
        String packageName = context.getVariable("packageName").toString();
        return path + "src\\main\\java\\" + packageName.replace(".", File.separator) + File.separator;
    }


    private Context getContext(Table table) {
        Map<String, Object> tableMap = new HashMap<>();
        GeneratorProperty generatorProperty = SpringUtil.getBean(GeneratorProperty.class);
        tableMap.put("packageName", generatorProperty.getPackageName());
        tableMap.put("table", table);

        //3.准备数据
        Context context = new Context();
        context.setVariables(tableMap);
        return context;
    }
}
