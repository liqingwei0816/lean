package com.guttv.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SysUser {


        
        /**
         *   
         */
        
        private Integer id;
        
        /**
         *   
         */
        
        private String userName;
        
        /**
         *   
         */
        private String password;
        
        /**
         *   是否可用
         */
        
        private Boolean available;
        
        /**
         *   注释
         */
        
        private String note;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}