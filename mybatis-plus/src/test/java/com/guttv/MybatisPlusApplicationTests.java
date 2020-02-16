package com.guttv;

import com.guttv.generator.entity.Table;
import com.guttv.generator.mapper.TableMapper;
import com.guttv.generator.service.TableService;
import com.guttv.generator.util.ThymeleafUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private TableMapper tableMapper;

    @Test
    public void testPath() {
        File file = new File("/D:/IdeaProjects/lean/mybatis-plus/com\\guttv\\bean");
        System.out.println(file.toPath().toUri().getPath());

    }
   /* @Test
    public void testFreeMack() throws IOException, TemplateException {
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

        String templateContent="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ADI Priority=\"1\" BizDomain=\"0\">\n" +
                "    <Objects>\n" +
                "        <Object>\n" +
                "            <Property Name=\"OrgAirDate\">${((orgairdate)?date?string('yyyyMMdd'))!?replace('-','')!''}</Property>\n" +
                "\t</Object>\n" +
                "    </Objects>\n" +
                "</ADI>";

        stringTemplateLoader.putTemplate("name",templateContent);
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setTemplateLoader(stringTemplateLoader);

        Template name = configuration.getTemplate("name");
        HashMap<String, Object> model = new HashMap<>();
        model.put("orgairdate", LocalDateTime.now());
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(name, model);

    }*/
    @Test
    public void test1() throws URISyntaxException, MalformedURLException {
        URI resource = ThymeleafUtil.class.getResource(".").toURI();
        System.out.println(resource.toURL().getPath());

    }

    @Resource
    private TableService tableService;
    @Resource
    private ThymeleafUtil thymeleafUtil;

    @Test
    public void testBean() {
        List<Table> tables = tableService.getTables();
        tables.forEach(e->{
            try {
                thymeleafUtil.createBean(e);
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });


    }
    @Test
    public void testMapperXml() {
        Table table = new Table();
        table.setName("t_user");
        try {
            thymeleafUtil.createMapper(table);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testHtml() {
        Table table = new Table();
        table.setName("t_user");
        try {
            thymeleafUtil.createController(table);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void testService() {
        List<Table> tables = tableService.getTables();
        tables.forEach(e->{
            try {
                thymeleafUtil.createBean(e);
                thymeleafUtil.createService(e);
                thymeleafUtil.createMapper(e);
                thymeleafUtil.createController(e);
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });


    }


}
