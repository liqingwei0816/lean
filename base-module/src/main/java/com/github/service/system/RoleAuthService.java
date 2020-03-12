package com.github.service.system;

import com.github.bean.system.RoleAuth;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RoleAuthService {

    List<RoleAuth> getList(RoleAuth roleAuth);

    RoleAuth getOne(RoleAuth roleAuth);

    Integer delete(Integer id);

    Integer insert(RoleAuth roleAuth);

    Integer updateById(@NotNull RoleAuth roleAuth);

    /**
     * 根据authId 删除RoleAuth绑定
     * @param authId 权限Id
     */
    void deleteByAuthId(Integer authId);
    /**
     * 根据roleId 删除RoleAuth绑定
     * @param roleId 角色Id
     */
    void deleteByRoleId(Integer roleId);
}
