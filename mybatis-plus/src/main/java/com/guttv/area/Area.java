package com.guttv.area;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *
 */
@Data
@TableName("t_area")
public class Area {

    /**
     * 自增id
     */
    private Integer id;
    /**
     * 省code
     */
    private String provinceCode;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 城市code
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 地区等级
     */
    private Integer level;
    /**
     * 首字母缩写
     */
    private String firstPinYin;
}
