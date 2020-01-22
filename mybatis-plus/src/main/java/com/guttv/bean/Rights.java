package com.guttv.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Rights {

    private Integer id;

    /**
     * 权益code
     */
    private String code;

    /**
     * 权益名称
     */
    private String name;

    /**
     * 权益备注
     */
    private String note;

    /**
     * 剩余次数
     */
    private Integer remains;

    /**
     * 状态、生效的：1，失效的：0
     */
    private Integer status;

    /**
     * 权益开始时间
     */
    private LocalDateTime startTime;

    /**
     * 权益结束时间 下线时间
     */
    private LocalDateTime endTime;

    /**
     * 折扣率
     */
    private Float discountRate;

    /**
     * 抵扣价格
     */
    private java.math.BigDecimal deductiblePrice;

    /**
     * 免费观看
     */
    private String freeViewing;

    /**
     * 线上权益，线下权益
     */
    private Integer online;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
