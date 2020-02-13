package com.guttv.service;

import com.guttv.bean.SysUser;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SysUserService {

    List<SysUser> getList(SysUser sysUser);

    SysUser getOne(SysUser sysUser);

    Integer delete(Integer id);

    Integer insert(SysUser sysUser);

    Integer updateById(@NotNull SysUser sysUser);

    SysUser getByUserName(String userName);
}
