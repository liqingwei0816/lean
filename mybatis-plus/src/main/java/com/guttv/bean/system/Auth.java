package com.guttv.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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
     *  权限代码
     */
    private String authCode;

    /**
     * 注释
     */

    private String note;
    /**
     * 是否展开
     */
    private Boolean spread;

    /**
     * 是否选中
     */
    private Boolean checked;


    /**
     * 上级节点
     */

    private Integer parentNode;

    private List<Auth> children;

    @JsonIgnore
    private List<Auth> allAuth;

    public List<Auth> getChildren() {
        if (allAuth == null || allAuth.isEmpty()) {
            return children;
        }
        return allAuth.stream().filter(e -> id.equals(e.parentNode)).collect(Collectors.toList());
    }

    @JsonIgnore
    private Integer pageNum;
    @JsonIgnore
    private Integer pageSize;
    @JsonIgnore
    private String orderBy;
}