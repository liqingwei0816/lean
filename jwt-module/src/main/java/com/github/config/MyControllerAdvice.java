package com.github.config;

import com.github.util.ResultUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
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
        ex.printStackTrace();
        return ResultUtils.error(ex.getMessage());
    }

}
