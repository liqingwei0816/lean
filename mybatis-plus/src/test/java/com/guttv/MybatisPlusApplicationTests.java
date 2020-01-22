package com.guttv;

import com.alibaba.fastjson.JSON;
import com.guttv.bean.Rights;
import com.guttv.generator.entity.Table;
import com.guttv.generator.mapper.TableMapper;
import com.guttv.generator.util.ThymeleafUtil;
import com.guttv.mapper.RightsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private TableMapper tableMapper;
    @Resource
    private RightsMapper rightsMapper;
    @Test
    public void test() {
        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<Rights> rights = rightsMapper.selectList();
        rights.forEach(e->System.out.println(JSON.toJSONString(e)));

    }
    @Test
    public void test1() throws URISyntaxException, MalformedURLException {
        URI resource = ThymeleafUtil.class.getResource("/aaa.text").toURI();
        System.out.println(resource.toURL().getPath());

    }

    @Test
    public void testBean() {
        Table table = new Table();
        table.setName("t_rights");
        table.setFields(tableMapper.getFields(table.getName()));

        StringBuilder bean = new StringBuilder();

        bean.append("package com.guttv.bean;\n\nimport lombok.Data;\n\n");
        //表备注
        if (!StringUtils.isEmpty(table.getComment())) {
            bean.append("/** \n     * \n").append(table.getComment()).append("\n     */\n");
        }
        //类体
        bean.append("@Data\npublic class ").append(table.getEntityName()).append(" {\n\n");

        table.getFields().forEach(e -> {
            if (!StringUtils.isEmpty(e.getComment())) {
                bean.append("    /**\n     * ").append(e.getComment()).append("\n     */\n");
            }
            bean.append("    private ").append(e.getJavaType()).append(" ").append(e.getName()).append(";\n\n");
        });
        bean.append("}");
        System.out.println(bean.toString());


    }


}
