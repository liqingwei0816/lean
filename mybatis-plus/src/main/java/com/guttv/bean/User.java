package com.guttv.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {


        
        /**
         *   
         */
        
        private Integer id;
        
        /**
         *   
         */
        
        private String name;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}