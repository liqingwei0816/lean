package com.guttv.service.impl.system;

import com.guttv.bean.system.Auth;
import com.guttv.bean.system.Role;
import com.guttv.bean.system.RoleAuth;
import com.guttv.controller.system.RoleController;
import com.guttv.mapper.system.RoleMapper;
import com.guttv.service.system.AuthService;
import com.guttv.service.system.RoleAuthService;
import com.guttv.service.system.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Object auth(Integer roleId) {
        Map<String, Object> data = new HashMap<>(2);
        List<Auth> list = authService.getAll();
        list.forEach(e -> e.setOpen(true));
        data.put("data",list);
        //拥有的权限
        List<Integer> authByRole = roleMapper.selectAuthByRole(roleId);
        authByRole.removeAll(list.stream().map(Auth::getParentNode).collect(Collectors.toList()));
        data.put("checkedList",authByRole);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeAuth(RoleController.RoleVo roleVo) {
        Integer roleId = roleVo.getRoleId();
        //删除角色下所有权限
        roleAuthService.deleteByRoleId(roleId);
        //添加现有权限
        roleVo.getAuthIds().forEach(e->{
            RoleAuth roleAuth = new RoleAuth();
            roleAuth.setRoleId(roleId);
            roleAuth.setAuthId(e);
            roleAuthService.insert(roleAuth);
        });
    }

}