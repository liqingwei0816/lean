package com.github.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * 用于为数据添加操作人修改人的切面 需要添加的bean需要实现Operator接口
 */
@Aspect
@Slf4j
public class OperatorAop {

    /**
     * 添加人添加切面
     */
    @Around("execution(public * com.github.quartz.mapper.*.*.insert*(..) )" +
            "||execution(public * com.github.quartz.mapper.*.insert*(..) )")
    public Object addOperator(ProceedingJoinPoint pjp) throws Throwable {
        //添加Operator操作人属性添加操作
        Object[] args = pjp.getArgs();
        try {
            //存在security环境时才处理
            ClassLoader.getSystemClassLoader().loadClass("org.springframework.security.core.context.SecurityContextHolder");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            if (args.length > 0 && args[0] instanceof Operator) {
                Operator operator = (OperatorAop.Operator) args[0];
                operator.setCreator(name);
                operator.setCreateTime(LocalDateTime.now());
            }
        } catch (ClassNotFoundException e) {
            //不存在security环境
            e.printStackTrace();
        }
        return pjp.proceed(args);
    }

    /**
     * 修改人添加切面
     */
    @Around("execution(public * com.github.quartz.mapper.*.*.update*(..) )" +
            "||execution(public * com.github.quartz.mapper.*.update*(..) )")
    public Object addUpdatePerson(ProceedingJoinPoint pjp) throws Throwable {
        //添加Operator操作人属性添加操作
        Object[] args = pjp.getArgs();
        try {
            //存在security环境时才处理
            ClassLoader.getSystemClassLoader().loadClass("org.springframework.security.core.context.SecurityContextHolder");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            if (args.length > 0 && args[0] instanceof Operator) {
                Operator operator = (OperatorAop.Operator) args[0];
                operator.setUpdatePerson(name);
                operator.setUpdateTime(LocalDateTime.now());
            }
        } catch (ClassNotFoundException e) {
            //不存在security环境
            e.printStackTrace();
        }
        return pjp.proceed(args);
    }


    /**
     *
     */
    public interface Operator {

        void setUpdatePerson(String updatePersonCode);

        void setUpdateTime(LocalDateTime updateTime);

        void setCreator(String creatorCode);

        void setCreateTime(LocalDateTime createTime);

    }


}
