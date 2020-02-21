package com.guttv.service.system;

import com.guttv.bean.system.Role;
import com.guttv.bean.system.SysUser;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SysUserService {

    List<SysUser> getList(SysUser sysUser);

    SysUser getOne(SysUser sysUser);

    Integer delete(Integer id);

    Integer insert(SysUser sysUser);

    Integer updateById(@NotNull SysUser sysUser);

    SysUser getByUserName(String userName);

    List<Role> getRoles(Integer sysUserId);
}
