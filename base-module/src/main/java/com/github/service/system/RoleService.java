package com.github.service.system;

import com.github.bean.system.Role;
import com.github.controller.system.RoleController;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RoleService {

    List<Role> getList(Role role);

    Role getOne(Role role);

    Integer delete(Integer id);

    Integer insert(Role role);

    Integer updateById(@NotNull Role role);

    /**
     * 根据roleId获取拥有的权限信息,返回全部权限信息,拥有的权限的id值
     * @param roleId 查询的roleId
     */
   Object auth(Integer roleId);

    void changeAuth(RoleController.RoleVo roleVo);
}
