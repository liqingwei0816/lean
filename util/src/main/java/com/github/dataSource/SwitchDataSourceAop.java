package com.github.dataSource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class SwitchDataSourceAop {


    /**
     * within类注解
     * annotation 方法注解
     */
    @Before("@within(SwitchDataSource)||@annotation(SwitchDataSource)")
    public void before(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取方法上的注解
        SwitchDataSource annotationClass = method.getAnnotation(SwitchDataSource.class);
        if (annotationClass == null) {
            //获取类上面的注解
            Class<?> aClass = joinPoint.getTarget().getClass();
            annotationClass = aClass.getAnnotation(SwitchDataSource.class);
            if (annotationClass == null){
                Class<?>[] interfaces = aClass.getInterfaces();
                for (Class<?> interfaceItem:interfaces){
                    annotationClass= interfaceItem.getAnnotation(SwitchDataSource.class);
                    if (annotationClass!=null){
                        break;
                    }
                }
                if (annotationClass==null){
                    return;
                }
            }
        }
        //获取注解上的数据源的值的信息
        String dataSourceKey = annotationClass.value();
        //给当前的执行SQL的操作设置特殊的数据源的信息
        DynamicDataSource.setDataSourceKey(dataSourceKey);
        log.info("AOP动态切换数据源，className {} methodName {} ;dataSourceKey: {}", joinPoint.getTarget().getClass().getName(),method.getName(),dataSourceKey);
    }
    /**
     * within类注解
     * annotation 方法注解
     */
    @After("@within(SwitchDataSource)||@annotation(SwitchDataSource)")
    public void after() {
        //移除当前线程的数据源设置
        DynamicDataSource.removeDataSourceKey();
        log.info("AOP动态切换数据源，移除数据源设置");
    }


}
