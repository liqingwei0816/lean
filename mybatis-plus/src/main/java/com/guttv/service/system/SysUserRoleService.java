package com.guttv.service.system;

import com.guttv.bean.system.SysUserRole;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SysUserRoleService {

    List<SysUserRole> getList(SysUserRole sysUserRole);

    SysUserRole getOne(SysUserRole sysUserRole);

    Integer delete(Integer id);

    Integer insert(SysUserRole sysUserRole);

    /**
     * 条件判断不存在才插入
     */
    void insertNoExists(SysUserRole sysUserRole);

    Integer updateById(@NotNull SysUserRole sysUserRole);

    void deleteBySysUserId(Integer sysUserId);
}
