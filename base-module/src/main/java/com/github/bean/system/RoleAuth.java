package com.github.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class RoleAuth {

        private Integer id;
        /**
         *   角色ID
         */
        
        private Integer roleId;
        
        /**
         *   权限Id
         */
        
        private Integer authId;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}