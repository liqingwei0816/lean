package com.github.config;

import com.github.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class MyControllerAdvice {

    /**
     * 全局异常捕捉处理
     * IllegalArgumentException 为Assert验证抛出的异常
     */
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultUtils errorHandler(IllegalArgumentException ex) {
        return ResultUtils.error(ex.getMessage());
    }
    /**
     * 全局异常捕捉处理
     * IllegalArgumentException 为Assert验证抛出的异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultUtils errorHandler(Exception ex) {
        log.error("系统异常",ex);
        return ResultUtils.error(ex.getMessage());
    }

}
