package com.guttv.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Role {


        
        /**
         *   
         */
        
        private Integer id;
        
        /**
         *   
         */
        
        private String roleName;
        
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