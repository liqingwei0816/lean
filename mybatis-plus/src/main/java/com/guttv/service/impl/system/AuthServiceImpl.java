package com.guttv.service.impl.system;

import com.guttv.bean.system.Auth;
import com.guttv.mapper.system.AuthMapper;
import com.guttv.service.system.AuthService;
import com.guttv.service.system.RoleAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthMapper authMapper;

    @Resource
    private RoleAuthService roleAuthService;

    @Override
    public List<Auth> getList(Auth auth) {
        return authMapper.selectList(auth);
    }

    @Override
    public Auth getOne(Auth auth) {
        return authMapper.selectOne(auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Integer id) {
        roleAuthService.deleteByAuthId(id);
        return authMapper.deleteById(id);
    }

    @Override
    public Integer updateById(@NotNull Auth auth) {
        return authMapper.updateById(auth);
    }

    @Override
    public Integer insert(@NotNull Auth auth) {
        return authMapper.insert(auth);
    }

    @Override
    public Long count(Auth auth) {
        return authMapper.selectCount(auth);
    }

}