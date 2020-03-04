package com.github.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SysUserRole {


        
        /**
         *   
         */
        
        private Integer id;
        
        /**
         *   角色ID
         */
        
        private Integer roleId;
        
        /**
         *   用户ID
         */
        
        private Integer sysUserId;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}