package com.github;

import com.github.compiler.DynamicCompiler;
import com.github.compiler.exceptions.DynamicCompilerException;
import com.github.compiler.impl.DynamicForwardingJavaFileManager;
import com.github.compiler.impl.JavaSourceObject;
import org.springframework.stereotype.Component;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.Locale;
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
        try {
            Boolean compile = compiler.compile("com.github.Pojo1", source);
            DynamicForwardingJavaFileManager javaFileManager = compiler.getJavaFileManager();
            DiagnosticCollector<JavaFileObject> diagnostic = compiler.getDiagnostic();
            diagnostic.getDiagnostics().forEach(s -> {
                String message = s.getMessage(Locale.getDefault());
                System.out.println(message);
            });
            Map<String, JavaSourceObject> fileOutObjects1 = javaFileManager.getFileOutObjects();
            for (String s : fileOutObjects1.keySet()) {
                JavaSourceObject javaFileObject = fileOutObjects1.get(s);
                byte[] byteCode = javaFileObject.getBytes();
                System.out.println(byteCode.length);
            }

        } catch (DynamicCompilerException e) {
            e.printStackTrace();
        }


    }


}
