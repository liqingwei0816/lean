package com.github.util;

import com.github.controller.quartz.JobVo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

/**
 * MisFire策略常量的定义在类CronTrigger中，列举如下：
 * <p>
 * MISFIRE_INSTRUCTION_FIRE_ONCE_NOW                 = 1 立即执行一次，然后按照Cron定义时间点执行
 * cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
 * MISFIRE_INSTRUCTION_DO_NOTHING                       = 2 什么都不做，等待Cron定义下次任务执行的时间点
 * cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
 * MISFIRE_INSTRUCTION_SMART_POLICY                    = 0 智能的策略，针对不同的Trigger执行不同，CronTrigger时为MISFIRE_INSTRUCTION_FIRE_ONCE_NOW
 * 默认值 无需设置 Builder没有对应方法
 * MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY    = -1 将所有错过的执行时间点全都补上，例如，任务15s执行一次，执行的任务错过了4分钟，则执行MisFire时，一次性执行4*(60/15)= 16次任务
 * cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires()
 */


@Component
@Slf4j
//依赖中存在quartz时加载
@ConditionalOnClass(Scheduler.class)
public class QuartzManager {

    private URLClassLoader classLoader;

    //注入调度器
    @Resource
    private Scheduler scheduler;

    public void addOrUpdate(JobVo jobVo) {
        try {
            addJob(jobVo);
        } catch (Exception e) {
            if (e.getMessage().equals("对应调度器已存在")) {
                modifyJob(jobVo);
                return;
            }
            log.error("job操作失败", e);
        }
    }


    /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobVo 自定义的job展示参数集合
     */

    public void addJob(JobVo jobVo) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:addJob  start");

        String jobClass = jobVo.getJobClass();
        //校验工作类
        Class  jobClassObj;
        try {
            jobClassObj= Class.forName(jobClass);
        }catch (Exception e){
            URL url = Paths.get(System.getProperty("user.dir")).toUri().toURL();
            if (classLoader!=null){
                classLoader.close();
            }
            classLoader = new URLClassLoader(new URL[]{url});
            jobClassObj= classLoader.loadClass(jobVo.getJobClass());
        }


        // 唯一主键
        String jobName = jobVo.getName();
        String jobGroup = jobVo.getGroup();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (scheduler == null) {
            log.info("scheduler is null");
        }
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (null != cronTrigger) {
            throw new Exception("对应调度器已存在");
        }

        //构建jobDetail
        JobDetail jobDetail = JobBuilder.newJob(jobClassObj).withIdentity(jobName, jobGroup)
                .withDescription(jobVo.getDescription()).storeDurably(jobVo.getDurability())
                .build();
        // 配置cron和misfire策略
        CronScheduleBuilder cronScheduleBuilder = getCronScheduleBuilder(jobVo);

        //构建cronTrigger
        cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .startNow()
                .withSchedule(cronScheduleBuilder)
                .usingJobData("data", jobVo.getJobData())
                .withDescription(jobVo.getDescription()).build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


    /**
     * 暂停一个任务
     *
     * @param jobVo 必须参数为 name group
     */

    public void pauseJob(JobVo jobVo) throws SchedulerException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:pasueOneJob  start");
        // 唯一主键
        TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getName(), jobVo.getGroup());
        Trigger trigger = scheduler.getTrigger(triggerKey);
        JobKey jobKey = trigger.getJobKey();
        scheduler.pauseJob(jobKey);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:pasueOneJob  end");
    }


    /**
     * 重启一个任务
     *
     * @param jobVo 必须参数为 name group
     */
    public void rebootJob(JobVo jobVo) throws SchedulerException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:resOneJob  start");
        // 唯一主键
        TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getName(), jobVo.getGroup());
        Trigger trigger = scheduler.getTrigger(triggerKey);
        scheduler.rescheduleJob(triggerKey, trigger);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:resOneJob  end");
    }


    /**
     * 修改一个任务
     */

    public void modifyJob(JobVo jobVo) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobTime  start");
        try {
            // 唯一主键
            String jobName = jobVo.getName();
            String jobGroup = jobVo.getGroup();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            cronTrigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(getCronScheduleBuilder(jobVo))
                    .usingJobData("data", jobVo.getJobData())
                    //设置Trigger的生效时间,防止立即执行
                    .startNow()
                    .build();
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobTime  end");
    }

    /**
     * 构建调度器
     *
     * @param jobVo 参数 必须为cron 可选为misfireInstruction
     */
    private CronScheduleBuilder getCronScheduleBuilder(JobVo jobVo) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobVo.getCron());
        if (!StringUtils.isEmpty(jobVo.getMisfireInstruction())) {
            switch (jobVo.getMisfireInstruction()) {
                case "DoNothing":
                    //DoNothing
                    cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
                    break;
                case "IgnoreMisfires":
                    //IgnoreMisfires
                    cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                    break;
                case "FireAndProceed":
                    //FireAndProceed
                    // cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                default:
                    //SMART_POLICY 默认值 无需设定
                    cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            }
        }
        return cronScheduleBuilder;

    }


    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     */

    public void removeJob(JobVo jobVo) throws SchedulerException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:removeJob  start");
        // 唯一主键
        String jobName = jobVo.getName();
        String jobGroup = jobVo.getGroup();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (null == trigger) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:removeJob trigger is NULL ");
            return;
        }
        JobKey jobKey = trigger.getJobKey();
        scheduler.pauseTrigger(triggerKey);// 停止触发器
        scheduler.unscheduleJob(triggerKey);// 移除触发器
        scheduler.deleteJob(jobKey);// 删除任务
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:removeJob  end");
    }
}

