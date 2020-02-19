package com.guttv.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class OperatorAop {


    @Around("execution(public * com.guttv.mapper.system.*.insert*(..))")
    public Object addOperator(ProceedingJoinPoint pjp) throws Throwable {
        //todo 添加Operator操作人属性添加操作
        Object[] args = pjp.getArgs();

        System.out.println("后通知");
        return pjp.proceed();
    }



}
