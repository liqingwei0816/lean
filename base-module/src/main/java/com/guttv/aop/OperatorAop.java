package com.guttv.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Aspect
@Slf4j
public class OperatorAop {

    /**
     * 添加人添加切面
     */
    @Around("execution(public * com.guttv.mapper.*.*.insert*(..) )" +
            "||execution(public * com.guttv.mapper.*.insert*(..) )")
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
     * 修改人人添加切面
     */
    @Around("execution(public * com.guttv.mapper.*.*.update*(..) )" +
            "||execution(public * com.guttv.mapper.*.update*(..) )")
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

    public interface Operator {

        void setUpdatePerson(String updatePersonCode);

        void setUpdateTime(LocalDateTime updateTime);

        void setCreator(String creatorCode);

        void setCreateTime(LocalDateTime createTime);

    }


}
