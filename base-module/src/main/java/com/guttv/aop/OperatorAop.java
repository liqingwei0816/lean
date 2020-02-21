package com.guttv.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        //todo 添加Operator操作人属性添加操作
        Object[] args = pjp.getArgs();
        try {
            //存在security环境时才处理
            ClassLoader.getSystemClassLoader().loadClass("org.springframework.security.core.context.SecurityContextHolder");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            if (args.length > 0) {
                Class<?> aClass = args[0].getClass();
                try {
                    //创建人
                    Method setCreator = aClass.getMethod("setCreator", String.class);
                    setCreator.invoke(args[0], name);
                    //创建时间
                    Method setCreateTime = aClass.getMethod("setCreateTime", LocalDateTime.class);
                    setCreateTime.invoke(args[0], LocalDateTime.now());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
                    log.debug("{}不存在creator属性", aClass.getName());
                }
            }
        } catch (ClassNotFoundException e) {
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
        //todo 添加Operator操作人属性添加操作
        Object[] args = pjp.getArgs();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (args.length > 0) {
            Class<?> aClass = args[0].getClass();
            try {
                //创建人
                Method setUpdatePerson = aClass.getMethod("setUpdatePerson", String.class);
                setUpdatePerson.invoke(args[0], name);
                //创建时间
                Method setUpdateTime = aClass.getMethod("setUpdateTime", LocalDateTime.class);
                setUpdateTime.invoke(args[0], LocalDateTime.now());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.debug("{}不存在updatePerson属性", aClass.getName(), e);
            }
        }

        return pjp.proceed(args);
    }


}
