package com.guttv.service.impl;

import com.guttv.bean.SysUser;
import com.guttv.mapper.SysUserMapper;
import com.guttv.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> getList(SysUser sysUser) {
        return sysUserMapper.selectList(sysUser);
    }

    @Override
    public SysUser getOne(SysUser sysUser) {
        return sysUserMapper.selectOne(sysUser);
    }

    @Override
    public Integer delete(Integer id) {
        return sysUserMapper.deleteById(id);
    }

    @Override
    public Integer insert(SysUser sysUser){
        return sysUserMapper.insert(sysUser);
    };

    @Override
    public Integer updateById(@NotNull SysUser sysUser) {
        return sysUserMapper.updateById(sysUser);
    }

    @Override
    public SysUser getByUserName(String userName) {
        SysUser sysUser =new SysUser();sysUser.setUserName(userName);
        return getOne(sysUser);
    }

}