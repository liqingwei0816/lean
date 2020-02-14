package com.guttv.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Role {


        
        /**
         *   
         */
        private Integer id;
        
        /**
         *   角色名
         */
        private String roleName;
        
        /**
         *   注释
         */
        private String note;

        /**
         *   是否选中
         */
        private Boolean checked;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}