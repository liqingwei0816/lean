package com.guttv.config;

import com.guttv.util.ResultUtils;
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

}
