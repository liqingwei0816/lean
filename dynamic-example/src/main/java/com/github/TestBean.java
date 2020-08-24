package com.github;

import com.github.compiler.DynamicCompiler;
import com.github.compiler.impl.JavaSourceObject;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class TestBean {

    public void test() {


        DynamicCompiler compiler = new DynamicCompiler();
        String source = "package com.github;\n" +
                "\n" +
                "public class Pojo1  extends TestPojo{\n" +
                "    private String bbb;\n" +
                "\n" +
                "    public String getBbb() {\n" +
                "        return bbb;\n" +
                "    }\n" +
                "\n" +
                "    public void setBbb(String bbb) {\n" +
                "        this.bbb = bbb;\n" +
                "    }\n" +
                "}\n";
        Boolean compile = compiler.compile(Collections.singletonList(new JavaSourceObject("com.github.Pojo1", source)));
        if (compile){
            Map<String, JavaSourceObject> fileOutObjects = compiler.getJavaFileManager().getFileOutObjects();
            fileOutObjects.forEach((key,fileOutObject)->{
                byte[] byteCode = fileOutObject.getBytes();
                System.out.println(key+""   +byteCode.length);
            });
        }
    }

}
