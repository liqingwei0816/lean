package com.guttv.service.impl.system;

import com.guttv.bean.system.RoleAuth;
import com.guttv.mapper.system.RoleAuthMapper;
import com.guttv.service.system.RoleAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class RoleAuthServiceImpl implements RoleAuthService {

    @Resource
    private RoleAuthMapper roleAuthMapper;

    @Override
    public List<RoleAuth> getList(RoleAuth roleAuth) {
        return roleAuthMapper.selectList(roleAuth);
    }

    @Override
    public RoleAuth getOne(RoleAuth roleAuth) {
        return roleAuthMapper.selectOne(roleAuth);
    }

    @Override
    public Integer delete(Integer id) {
        return roleAuthMapper.deleteById(id);
    }

    @Override
    public Integer insert(RoleAuth roleAuth){
        return roleAuthMapper.insert(roleAuth);
    }

    @Override
    public Integer updateById(@NotNull RoleAuth roleAuth) {
        return roleAuthMapper.updateById(roleAuth);
    }

    @Override
    public void deleteByAuthId(Integer authId) {
        roleAuthMapper.deleteByAuthId(authId);
    }
    @Override
    public void deleteByRoleId(Integer roleId) {
        roleAuthMapper.deleteByRoleId(roleId);
    }

}