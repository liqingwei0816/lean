/*
package com.github;

import com.github.util.CompilerUtil;

import java.io.IOException;
import java.nio.file.Path;

public class Test {


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //集群模式是文件放入本地  无法集群处理 下部放入数据库
        String fileName = "SampleJob1.java";
        String classContent =
                "package com.github.job;\n" +
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
                        "    public void setName(String name) {\n" +
                        "    }\n" +
                        "\n" +
                        "    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {\n" +
                        "        Long count = this.authService.count(new Auth());\n" +
                        "        log.error(\"{} job1 {}\", count, context.getScheduledFireTime());\n" +
                        "    }\n" +
                        "}\n";


        //1 创建java文件
        Path javaFile = CompilerUtil.createJavaFile(fileName, classContent);
        //2 编译Java文件 获取class文件
        CompilerUtil.compiler(javaFile);
        //3. 获取class类对象
        //获取包名
        String[] split = classContent.split(";", 2);
       // String classFullName = split[0].replaceFirst("package", "").trim() + "." + fileName.replace(".java", "");

        Class<?> aClass = CompilerUtil.loadClass1("com.github.job.SampleJob1");






    }
}
*/
