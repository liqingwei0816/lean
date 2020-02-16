package com.guttv.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Auth {


    /**
     *
     */
    private Integer id;

    /**
     * 路径
     */
    private String url;

    /**
     * 权限名
     */
    private String title;
    /**
     * 权限代码
     */
    private String authCode;

    /**
     * 注释
     */
    private String note;
    /**
     * 是否展开
     */
    private Boolean open;

    /**
     * 是否选中
     */
    private Boolean checked;


    /**
     * 上级节点
     */
    private Integer parentNode;

    @JsonIgnore
    private Integer pageNum;
    @JsonIgnore
    private Integer pageSize;
    @JsonIgnore
    private String orderBy;
}