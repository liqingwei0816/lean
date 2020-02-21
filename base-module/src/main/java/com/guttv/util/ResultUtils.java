package com.guttv.util;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultUtils {
    private Integer code;
    private String msg;
    private Long count;
    private Object data;
    public static ResultUtils result(Integer code , String message , Object data){
        return new ResultUtils(code , message , data);
    }
    public static ResultUtils success(Object data){
        return new ResultUtils(0, "success", data);
    }
    public static <T> ResultUtils success(PageInfo<T> data){
        return new ResultUtils(0, "success", data.getTotal(),data.getList());
    }
    public static ResultUtils error(String message){
        return new ResultUtils(-1, message,null);
    }
    public static ResultUtils error(){
        return new ResultUtils(-1, "error",null);
    }
    public static ResultUtils error(Integer code,String message){
        return new ResultUtils(code, message,null);
    }
    public static ResultUtils success(String message){
        return new ResultUtils(0 , message ,null);
    }
    public static ResultUtils success(String message,Object data){
        return new ResultUtils(0 , message ,data);
    }
    public static ResultUtils success(){
        return new ResultUtils(0 , "success" ,null);
    }

    public ResultUtils(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
