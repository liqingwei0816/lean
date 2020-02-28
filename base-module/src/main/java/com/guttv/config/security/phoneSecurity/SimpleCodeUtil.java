package com.guttv.config.security.phoneSecurity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CodeUtil默认实现
 */
public class SimpleCodeUtil implements CodeUtil {

    private Map<String,CodeEntity> codeMap=new ConcurrentHashMap<>();

    @Override
    public String getCode(String key) {
        CodeEntity codeEntity = codeMap.get(key);
        if (codeEntity!=null){
            LocalDateTime localDateTime = codeEntity.getCreateTime().plusMinutes(10);
            if (localDateTime.isAfter(LocalDateTime.now())){
                return codeEntity.getValue();
            }
        }
        return null;
    }

    @Override
    public void addCode(String key, String code) {
        codeMap.put(key,new CodeEntity(code, LocalDateTime.now()));
    }

    @Override
    public void removeCode(String key) {
        codeMap.remove(key);
    }
    @Data
    @AllArgsConstructor
    static class CodeEntity{
        private String value;
        private LocalDateTime createTime;
    }
}
