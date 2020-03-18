package com.github;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Test1 {

    public static void main(String[] args) throws Exception {
        Test1 test1 = new Test1();
        test1.createJavaFile();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int run = compiler.run(null, null, null, path.toString());
        if (run == 0) { // 0为编译成功，1为报错
            test1.loadProxy();
        }
        test1.loadProxy();
    }
    static Path path = Paths.get("/SampleJob1.java");

    /**
     * 反射调用方法
     */
    private  void loadProxy() throws Exception {
        URL url = Paths.get(System.getProperty("user.dir")).toUri().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
        Class<?> autoProxy = loader.loadClass("com.github.job.SampleJob1") ;
        Object o = autoProxy.newInstance();
    }


    /**
     * 创建java文件
     * @throws IOException
     */
    private  void createJavaFile() throws IOException {
        String src = "package com.github.job;\n" +
                "\n" +
                "import com.github.bean.system.Auth;\n" +
                "import com.github.service.system.AuthService;\n" +
                "import javax.annotation.Resource;\n" +
                "import org.quartz.JobExecutionContext;\n" +
                "import org.quartz.JobExecutionException;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "import org.springframework.scheduling.quartz.QuartzJobBean;\n" +
                "\n" +
                "public class SampleJob1 extends QuartzJobBean {\n" +
                "    private static final Logger log = LoggerFactory.getLogger(SampleJob1.class);\n" +
                "    @Resource\n" +
                "    private AuthService authService;\n" +
                "    private String name;\n" +
                "\n" +
                "    public SampleJob1() {\n" +
                "    }\n" +
                "\n" +
                "    public void setName(String var1) {\n" +
                "    }\n" +
                "\n" +
                "    protected void executeInternal(JobExecutionContext var1) throws JobExecutionException {\n" +
                "        Long var2 = this.authService.count(new Auth());\n" +
                "        log.error(\"{} job1 {}\", var2, var1.getScheduledFireTime());\n" +
                "    }\n" +
                "}" ;

        //Files.createDirectories(path.getParent());
        Files.write(path,src.getBytes(),StandardOpenOption.CREATE) ;
    }
}
