package com.github.controller;

import lombok.Data;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class JobVo {
    /**
     * 名称 job和 trigger使用相同的名称
     */
    private String name;
    /**
     * 组名 job和 trigger使用相同的组名
     */
    private String group;
    /**
     * 状态 rigger当前状态
     */
    private String state;
    /**
     * 备注
     */
    private String description = "";
    /**
     * 工作类
     */
    private String jobClass;
    /**
     * 工作类编码 用于动态添加编译job工作类
     */
    private String jobClassContent;
    /**
     * 工作类字节码
     */
    private byte[] jobClassByteCode;
    /**
     * 耐久性 为false时 当job未关联到trigger时会自动删除
     */
    private Boolean durability = false;
    /**
     * 是否不允许并发执行
     * 为true，则不允许一个QuartzJob并发执行，否则就是顺序执行
     * 例如QuartzJob A执行时间为15秒，配置为每10秒执行一次；
     * 如果concurrent为true，则0秒的时候启动一次A，10秒的时候再启动一次A，20秒的时候再启动一次A，不管前面启动的A有没有执行完；
     * 如果concurrent为false，则0秒的时候启动一次A，15秒的时候A执行完毕，再第二次启动A。
     */
    private Boolean concurrentExecutionDisallowed;
    /**
     * cron
     */
    private String cron;
    /**
     * 失火策略
     */
    private String misfireInstruction;
    /**
     * 任务参数 json格式
     */
    private String jobData;
    /**
     * 上次执行时间
     */
    private Date previousFireTime;
    /**
     * 下次执行时间
     */
    private Date nextFireTime;


    public String getState() {
        return JobStateEnum.getValueByName(state);
    }

    public static enum MisfireInstructionEnum {
        /*
         * MisFire策略常量的定义在类CronTrigger中，列举如下：
         *
         * MISFIRE_INSTRUCTION_FIRE_ONCE_NOW                 = 1 立即执行一次，然后按照Cron定义时间点执行
         *  cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
         * MISFIRE_INSTRUCTION_DO_NOTHING                       = 2 什么都不做，等待Cron定义下次任务执行的时间点
         *  cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
         * MISFIRE_INSTRUCTION_SMART_POLICY                    = 0 智能的策略，针对不同的Trigger执行不同，CronTrigger时为MISFIRE_INSTRUCTION_FIRE_ONCE_NOW
         *  默认值 无需设置 Builder没有对应方法
         * MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY    = -1 将所有错过的执行时间点全都补上，例如，任务15s执行一次，执行的任务错过了4分钟，则执行MisFire时，一次性执行4*(60/15)= 16次任务
         *  cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires()
         *
         */
        SMART_POLICY(0),   FireAndProceed(1), DoNothing(2),IgnoreMisfires(-1);
        public Integer value;
        MisfireInstructionEnum(int value) {
            this.value = value;
        }

        static String getNameByValue(int value) {
            Optional<MisfireInstructionEnum> any = Stream.of(MisfireInstructionEnum.values()).filter(e -> e.value == value).findAny();
            return any.orElse(SMART_POLICY).name();
        }
    }


    @SuppressWarnings("unused")
    public  static enum JobStateEnum {
        /*
         * MisFire策略常量的定义在类CronTrigger中，列举如下：
         *
         * MISFIRE_INSTRUCTION_FIRE_ONCE_NOW                 = 1 立即执行一次，然后按照Cron定义时间点执行
         *  cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
         * MISFIRE_INSTRUCTION_DO_NOTHING                       = 2 什么都不做，等待Cron定义下次任务执行的时间点
         *  cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
         * MISFIRE_INSTRUCTION_SMART_POLICY                    = 0 智能的策略，针对不同的Trigger执行不同，CronTrigger时为MISFIRE_INSTRUCTION_FIRE_ONCE_NOW
         *  默认值 无需设置 Builder没有对应方法
         * MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY    = -1 将所有错过的执行时间点全都补上，例如，任务15s执行一次，执行的任务错过了4分钟，则执行MisFire时，一次性执行4*(60/15)= 16次任务
         *  cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires()
         *
         */
       NONE("不存在"), NORMAL("正常"), PAUSED("暂停"), COMPLETE("完成"), ERROR("错误"), BLOCKED("阻塞");
        public String value;
        JobStateEnum(String value) {
            this.value = value;
        }
        static String getValueByName(String name) {
            Optional<JobStateEnum> any = Stream.of(JobStateEnum.values()).filter(e -> e.name().equals(name)).findAny();
            return any.orElse(NORMAL).value;
        }
    }
}

