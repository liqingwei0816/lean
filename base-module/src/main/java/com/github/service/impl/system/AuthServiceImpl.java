package com.github.service.impl.system;

import com.github.bean.system.Auth;
import com.github.quartz.mapper.system.AuthMapper;
import com.github.service.system.AuthService;
import com.github.service.system.RoleAuthService;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "auth", unless = "#result == null")
    public List<Auth> getList(Auth auth) {
        return authMapper.selectList(auth);
    }

    @Override
    @Cacheable(value = "auth", unless = "#result == null")
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

    @Override
    @Cacheable(value = "auth",key = "'auth-'+#userName")
    public List<Auth> authoritiesByUsernameQuery(@NotNull String userName) {
        return authMapper.authoritiesByUsernameQuery(userName);
    }

}