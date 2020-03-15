package com.github.config.quartz;

import com.github.job.SampleJob;
import org.quartz.SchedulerContext;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class SpringBeanJobFactoryCustomizer extends SpringBeanJobFactory {
    @Nullable
    private String[] ignoredUnknownProperties;

    @Nullable
    private ApplicationContext applicationContext;

    @Nullable
    private SchedulerContext schedulerContext;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object job;

        try {
            job = (this.applicationContext != null ?
                    this.applicationContext.getAutowireCapableBeanFactory().createBean(
                            bundle.getJobDetail().getJobClass(), AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false) :
                    super.createJobInstance(bundle));
        }catch (Exception e){
            //toDO 添加动态加载job类的获取操作
            job=new SampleJob();
        }



        if (isEligibleForPropertyPopulation(job)) {
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(job);
            MutablePropertyValues pvs = new MutablePropertyValues();
            if (this.schedulerContext != null) {
                pvs.addPropertyValues(this.schedulerContext);
            }
            pvs.addPropertyValues(bundle.getJobDetail().getJobDataMap());
            pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());
            if (this.ignoredUnknownProperties != null) {
                for (String propName : this.ignoredUnknownProperties) {
                    if (pvs.contains(propName) && !bw.isWritableProperty(propName)) {
                        pvs.removePropertyValue(propName);
                    }
                }
                bw.setPropertyValues(pvs);
            } else {
                bw.setPropertyValues(pvs, true);
            }
        }

        return job;
    }
}
