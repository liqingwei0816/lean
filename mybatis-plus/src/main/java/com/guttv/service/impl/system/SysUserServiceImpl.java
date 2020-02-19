package com.guttv.service.impl.system;

import com.guttv.bean.system.Role;
import com.guttv.bean.system.SysUser;
import com.guttv.mapper.system.SysUserMapper;
import com.guttv.service.system.SysUserRoleService;
import com.guttv.service.system.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Override
    public List<SysUser> getList(SysUser sysUser) {
        return sysUserMapper.selectList(sysUser);
    }

    @Override
    public SysUser getOne(SysUser sysUser) {
        return sysUserMapper.selectOne(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Integer id) {
        sysUserRoleService.deleteBySysUserId(id);
        return sysUserMapper.deleteById(id);
    }

    @Override
    public Integer insert(SysUser sysUser){
        return sysUserMapper.insert(sysUser);
    }

    @Override
    public Integer updateById(@NotNull SysUser sysUser) {
        return sysUserMapper.updateById(sysUser);
    }

    @Override
    public SysUser getByUserName(String userName) {
        SysUser sysUser =new SysUser();sysUser.setUserName(userName);
        return getOne(sysUser);
    }

    @Override
    public List<Role> getRoles(Integer sysUserId) {
        return this.sysUserMapper.selectRoles(sysUserId);
    }

}