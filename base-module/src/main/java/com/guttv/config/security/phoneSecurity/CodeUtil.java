package com.guttv.config.security.phoneSecurity;

public interface CodeUtil {

    /**
     * 获取验证码接口
     * @param key 验证码标识
     * @return 验证码
     */
    String getCode(String key);
    /**
     * 获取验证码接口
     * @param key 验证码标识
     */
    void addCode(String key,String code);
    /**
     * 获取验证码接口
     * @param key 验证码标识
     */
    void removeCode(String key);

}
