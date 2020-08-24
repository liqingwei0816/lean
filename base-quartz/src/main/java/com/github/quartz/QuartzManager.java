package com.github.quartz;

import com.github.compiler.CompilerUtil;
import com.github.quartz.classload.JobClassLoader;
import com.github.controller.JobVo;
import com.github.quartz.mapper.DynamicJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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

//todo 添加log
//todo 添加job异常监听
@Component
@Slf4j
//依赖中存在quartz时加载
@ConditionalOnClass(Scheduler.class)
public class QuartzManager {

    //注入调度器
    @Resource
    private Scheduler scheduler;
    //注入动态编译的job类操作接口
    @Resource
    private DynamicJobService dynamicJobService;

    private static JobClassLoader jobClassLoader;

    private static LocalDateTime jobClassLoaderExpireTime;

    public static synchronized JobClassLoader getJobClassLoader() {
        if (jobClassLoader == null || jobClassLoaderExpireTime.isBefore(LocalDateTime.now())) {
            //todo 待添加更新间隔可配置方式 通知方式
            jobClassLoaderExpireTime = LocalDateTime.now().plusSeconds(10);
            jobClassLoader = new JobClassLoader();
        }
        return jobClassLoader;
    }

    /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobVo 自定义的job展示参数集合
     */
    @SuppressWarnings("unchecked")
    public void addJob(JobVo jobVo) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:addJob  start");

        // 唯一主键
        String jobName = jobVo.getName();
        String jobGroup = jobVo.getGroup();
        //1 校验 triggerKey是否已存在
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (scheduler == null) {
            log.info("scheduler is null");
        }
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (null != cronTrigger) {
            throw new Exception("对应调度器已存在");
        }
        //2 校验工作类是否存在
        String jobClass = jobVo.getJobClass();

        Class<? extends Job> jobClassObj;
        //添加动态编译操作
        if (!StringUtils.isEmpty(jobVo.getJobClassContent())) {
            try {
                getJobClassLoader().loadClass(jobClass);
                throw new Exception("对应类已存在：" + jobClass);
            } catch (ClassNotFoundException ignore) {
            }

            //动态编译  内存中编译 获取字节码
            byte[] bytes = CompilerUtil.compileOne(jobClass, jobVo.getJobClassContent());
            jobVo.setJobClassByteCode(bytes);
            //动态job入库
            dynamicJobService.insert(jobVo);
        }
        jobClassObj = (Class<? extends Job>) getJobClassLoader().loadClass(jobClass);

        // 构建jobDetail
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
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:pauseOneJob  start");
        // 唯一主键
        TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getName(), jobVo.getGroup());
        Trigger trigger = scheduler.getTrigger(triggerKey);
        JobKey jobKey = trigger.getJobKey();
        scheduler.pauseJob(jobKey);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:pauseOneJob  end");
    }

    /**
     * 暂停一个任务
     *
     * @param jobVo 必须参数为 name group
     */
    public void runJob(JobVo jobVo) throws SchedulerException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:runOneJob  start");
        // 唯一主键
        JobKey jobKey = JobKey.jobKey(jobVo.getName(), jobVo.getGroup());
        scheduler.triggerJob(jobKey);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:runOneJob  end");
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
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
        //设定生效时间,防止立即执行
        trigger = trigger.getTriggerBuilder().startNow().build();
        //删除job
        scheduler.deleteJob(trigger.getJobKey());
        //添加job
        scheduler.scheduleJob(jobDetail, trigger);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:resOneJob  end");
    }

    /**
     * 修改一个任务
     */
    public void modifyJob(JobVo jobVo) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJob  start");
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
            if (!StringUtils.isEmpty(jobVo.getJobClassContent())) {
                byte[] bytes = CompilerUtil.compileOne(jobVo.getJobClass(), jobVo.getJobClassContent());
                jobVo.setJobClassByteCode(bytes);
                dynamicJobService.update(jobVo);
            }
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJob  end");
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

