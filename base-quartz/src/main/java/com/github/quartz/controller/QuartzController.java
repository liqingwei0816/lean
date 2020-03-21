package com.github.quartz.controller;

import com.github.quartz.mapper.DynamicJobService;
import com.github.quartz.QuartzManager;
import com.github.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ConditionalOnClass(value = {Scheduler.class})
@RequestMapping("quartz")
public class QuartzController {

    @Resource
    private Scheduler scheduler;

    @Resource
    private QuartzManager quartzManager;


    @RequestMapping("jobs")
    public Object jobs(String name) throws SchedulerException {
        List<JobVo> collect = scheduler.getTriggerKeys(GroupMatcher.anyGroup()).stream()
                .filter(triggerKey -> triggerKey.getName().toLowerCase().contains(name==null?"":name.toLowerCase()))
                .map(this::triggerKey2Job).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    @RequestMapping("list")
    public ModelAndView listPage(ModelAndView modelAndView) {
        modelAndView.setViewName("quartz/list");
        return modelAndView;
    }

    @RequestMapping("{operation}")
    public ResultUtils deleteJob(@RequestBody JobVo jobVo, @PathVariable String operation) {

        try {
            switch (operation){
                case "delete":quartzManager.removeJob(jobVo);break;
                case "pause":quartzManager.pauseJob(jobVo);break;
                case "reboot":quartzManager.rebootJob(jobVo);break;
                case "add":quartzManager.addJob(jobVo);break;
                case "update":quartzManager.modifyJob(jobVo);break;
                case "run":quartzManager.runJob(jobVo);break;
                default: throw new Exception("不支持的操作");
            }
            return ResultUtils.success();
        } catch (Exception e) {
            return ResultUtils.error("job删除操作失败\n"+e.getMessage());
        }
    }

    @Resource
    private DynamicJobService dynamicJobService;

    /**
     *
     * @param triggerKey 由triggerKey 获取jobVo
     * @return JobVo
     */
    private JobVo triggerKey2Job(TriggerKey triggerKey) {
        JobVo job = new JobVo();
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
            if (jobDetail instanceof JobDetailImpl) {
                JobDetailImpl jobDetailImpl = (JobDetailImpl) jobDetail;
                job.setName(jobDetailImpl.getName());
                job.setGroup(jobDetailImpl.getGroup());
                job.setDescription(jobDetailImpl.getDescription());
                job.setJobClass(jobDetailImpl.getJobClass().getName());
                job.setDurability(jobDetailImpl.isDurable());
                //添加工作类内容
                String jobClassContent = dynamicJobService.selectContentByClassName(jobDetailImpl.getJobClass().getName());
                job.setJobClassContent(jobClassContent);
                //设置方式为使用DisallowConcurrentExecution注解
                job.setConcurrentExecutionDisallowed(jobDetailImpl.isConcurrentExectionDisallowed());
                Object data=trigger.getJobDataMap().get("data");
                if (data!=null){
                    job.setJobData(data.toString());
                }

                if (trigger instanceof CronTrigger) {
                    job.setCron(((CronTrigger) trigger).getCronExpression());
                    job.setPreviousFireTime(trigger.getPreviousFireTime());
                    job.setNextFireTime(trigger.getNextFireTime());
                    job.setMisfireInstruction(JobVo.MisfireInstructionEnum.getNameByValue(trigger.getMisfireInstruction()));
                    job.setState(scheduler.getTriggerState(triggerKey).name());
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return job;
    }


}
