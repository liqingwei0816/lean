package com.guttv.service;

import com.guttv.bean.Auth;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public interface AuthService {

    List<Auth> getList(Auth auth);
    default List<Auth> getAll(){
        return getList(new Auth());
    }

    Auth getOne(Auth auth);

    Integer delete(@NotNull Integer id);

    Integer updateById(@NotNull Auth auth);

    Integer insert(Auth auth);

    /**
     * 获取树型节点数据 并为父节点赋予展开属性 参数必须包含id 和 parentNode
     */
    default List<Auth> getTreeAndSpreadParent(Auth auth) {
        List<Auth> list = getList(new Auth());
        list.forEach(e -> e.setAllAuth(list));
        list.stream().filter(e-> e.getId().equals(auth.getId())).forEach(this::setSpread);
        return list.stream().filter(e -> e.getParentNode() == null).collect(Collectors.toList());
    }

    /*
     * 级联展示
     */
    default void setSpread(Auth auth) {
        auth.setSpread(true);
        if (auth.getParentNode() != null) {
            auth.getAllAuth().stream().filter(e -> e.getId().equals(auth.getParentNode())).forEach(this::setSpread);
        }
    }

    void deleteBondingByRoleId(Integer id);
}
