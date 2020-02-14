package com.guttv.service.impl.system;

import com.guttv.bean.system.Auth;
import com.guttv.bean.system.Role;
import com.guttv.mapper.RoleMapper;
import com.guttv.service.system.AuthService;
import com.guttv.service.system.RoleAuthService;
import com.guttv.service.system.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleAuthService roleAuthService;
    @Resource
    private AuthService authService;

    @Override
    public List<Role> getList(Role role) {
        return roleMapper.selectList(role);
    }

    @Override
    public Role getOne(Role role) {
        return roleMapper.selectOne(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Integer id) {
        roleAuthService.deleteByRoleId(id);
        return roleMapper.deleteById(id);
    }

    @Override
    public Integer insert(Role role){
        return roleMapper.insert(role);
    }

    @Override
    public Integer updateById(@NotNull Role role) {
        return roleMapper.updateById(role);
    }

    @Override
    public List<Auth> auth(Integer roleId) {
        List<Auth> list = authService.getAll();
        //拥有的权限
        List<Integer> authByRole = roleMapper.selectAuthByRole(roleId);
        Set<Integer> parentNode = list.stream().map(Auth::getParentNode).collect(Collectors.toSet());
        list.forEach(e ->{
            e.setAllAuth(list);
            e.setChecked(authByRole.contains(e.getId())&&!parentNode.contains(e.getId()));
            e.setSpread(true);
        });
        return list.stream().filter(e -> e.getParentNode() == null).collect(Collectors.toList());
    }

}