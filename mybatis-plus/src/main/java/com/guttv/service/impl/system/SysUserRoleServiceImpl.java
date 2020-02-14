package com.guttv.service.impl.system;

import com.guttv.bean.system.SysUserRole;
import com.guttv.mapper.SysUserRoleMapper;
import com.guttv.service.system.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysUserRole> getList(SysUserRole sysUserRole) {
        return sysUserRoleMapper.selectList(sysUserRole);
    }

    @Override
    public SysUserRole getOne(SysUserRole sysUserRole) {
        return sysUserRoleMapper.selectOne(sysUserRole);
    }

    @Override
    public Integer delete(Integer id) {
        return sysUserRoleMapper.deleteById(id);
    }

    @Override
    public Integer insert(SysUserRole sysUserRole){
        return sysUserRoleMapper.insert(sysUserRole);
    }

    @Override
    public void insertNoExists(SysUserRole sysUserRole) {
        this.sysUserRoleMapper.insertNoExists(sysUserRole);
    }


    @Override
    public Integer updateById(@NotNull SysUserRole sysUserRole) {
        return sysUserRoleMapper.updateById(sysUserRole);
    }

}