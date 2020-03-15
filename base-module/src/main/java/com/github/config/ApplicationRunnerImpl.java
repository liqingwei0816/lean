/*
package com.github.config;

import com.github.controller.quartz.JobVo;
import com.github.util.CompilerUtil;
import com.github.util.QuartzManager;
import org.quartz.Scheduler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Resource
    private CacheManager cacheManager;
    @Resource
    private QuartzManager quartzManager;
    @Resource
    private Scheduler scheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
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
        CompilerUtil compilerUtil = new CompilerUtil();
        String jobClassName = compilerUtil.getClassObjectByJavaFile(fileName, classContent);

        JobVo jobVo = new JobVo();
        jobVo.setCron("0/5 * * * * ?");
        jobVo.setJobClass(jobClassName);
        jobVo.setName("SampleJob1");
        jobVo.setGroup("SampleJob1");
        quartzManager.addJob(jobVo);
        System.out.println(cacheManager.getClass().getName());

    }
}

*/
