package com.guttv.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MemberType {


        
        /**
         *   
         */
        
        private Integer id;
        
        /**
         *   会员类型编码
         */
        
        private String code;
        
        /**
         *   会员类型名
         */
        
        private String name;
        
        /**
         *   付费用户
         */
        
        private Integer payment;
        
        /**
         *   绑定的产品，免费会员成为付费会员购买的商品名称
         */
        
        private String boundProduct;
        
        /**
         *   状态，生效：1、失效：0
         */
        
        private Integer state;
        
        /**
         *   创建时间
         */
        
        private java.time.LocalDateTime createTime;
        
        /**
         *   更新时间
         */
        
        private java.time.LocalDateTime updateTime;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}