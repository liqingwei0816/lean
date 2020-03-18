package com.github;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Test2 {

    private static String baseDir="/";

    public static void main(String[] args) throws Exception{
        URL url = Paths.get("/").toAbsolutePath().toUri().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
       String className="com.github.job.SampleJob1";
        //1 类字符串输出到文件
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
                "        log.error(\"{} job33 {}\", var2, var1.getScheduledFireTime());\n" +
                "    }\n" +
                "}" ;

        //Path filePath = createJavaFile(className, src);
        //2 编译文件-》.class
        //compiler(filePath);
        //3 类加载器
        Class<?> aClass1 = Class.forName(className, true, classLoader);
        Class<?> aClass = classLoader.loadClass(className);

        System.out.println(aClass.getName());

       /* //获得系统的java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //编译文件，编译成功返回 0 否则 返回 1
        int flag = compiler.run(null, null, null,"D:\\com\\github\\job\\SampleJob1.java");
        System.out.println(flag == 0 ? "编译成功" : "编译失败");
        //指定class路径，默认和源代码路径一致，加载class
        Object printer = classLoader.loadClass("com.github.job.SampleJob1").newInstance();*/
        //System.out.println(printer.toString());

    }


    public static void compiler(Path path) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int flag = compiler.run(null, null, null, path.toFile().getAbsolutePath());
            System.out.println(flag == 0 ? "编译成功" : "编译失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 将class字符串放入磁盘
     *
     * @param fileName    文件名 默认上级目录为 System.getProperty("user.dir")
     * @param classString class内容
     */
    public static Path createJavaFile(String fileName, String classString) throws IOException {
        String filePath = fileName.replace(".", File.separator);
        Path path = Paths.get(baseDir + filePath + ".java");
        return Files.write(path, classString.getBytes(), StandardOpenOption.CREATE);
    }
}
