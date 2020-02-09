package com.guttv.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ClockSign {


        
        /**
         *   自增id
         */
        
        private Integer id;
        
        /**
         *   唯一code
         */
        
        private String code;
        
        /**
         *   会员code
         */
        
        private String memberCode;
        
        /**
         *   签到的年月
         */
        
        private String dateMonth;
        
        /**
         *   签到数据
         */
        
        private Integer mask;
        
        /**
         *   连续签到月数
         */
        
        private Integer continueSignMonth;
        
        /**
         *   签到时间
         */
        
        private java.time.LocalDateTime signTime;

        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}