package com.github;

import com.github.compiler.DynamicCompiler;
import com.github.compiler.exceptions.DynamicCompilerException;
import com.github.compiler.impl.DynamicForwardingJavaFileManager;
import com.github.compiler.impl.DynamicSimpleFileObject;
import org.springframework.stereotype.Component;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.Arrays;
import java.util.Map;

@Component
public class TestBean {

    public void test(){

        DynamicCompiler<Object> compiler = new DynamicCompiler<>(getClass().getClassLoader(), Arrays.asList("-target", "1.8"));
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
        try {
            Boolean compile = compiler.compile("com.github.Pojo1", source);
            DynamicForwardingJavaFileManager javaFileManager = compiler.getJavaFileManager();
            Map<String, JavaFileObject> fileOutObjects1 = javaFileManager.getFileOutObjects();
            for (String s : fileOutObjects1.keySet()) {
                JavaFileObject javaFileObject = fileOutObjects1.get(s);
                if (javaFileObject instanceof DynamicSimpleFileObject){
                    byte[] byteCode = ((DynamicSimpleFileObject) javaFileObject).getByteCode();
                    System.out.println(byteCode.length);
                }
            }

        } catch (DynamicCompilerException e) {
            e.printStackTrace();
        }




    }



}
