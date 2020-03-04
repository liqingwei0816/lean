package com.github.config.security.jwt;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecretKeyUtil {
    private Map<String, String> map = new ConcurrentHashMap<>();

    public String getSecretKey(String name) {

        return map.getOrDefault(name, setSecretKey(name));
    }

    public String setSecretKey(String name) {
        //数据库中获取
        String secretKey = JWTUtil.secretKey;
        map.put(name, secretKey);
        return secretKey;
    }

}
