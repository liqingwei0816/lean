package com.guttv.service.impl;

import com.guttv.bean.Auth;
import com.guttv.mapper.AuthMapper;
import com.guttv.service.AuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthMapper authMapper;

    @Override
    public List<Auth> getList(Auth auth) {
        return authMapper.selectList(auth);
    }

    @Override
    public Auth getOne(Auth auth) {
        return authMapper.selectOne(auth);
    }

    @Override
    public Integer delete(Integer id) {
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
    public void deleteBondingByRoleId(Integer id) {
        authMapper.deleteBondingByRoleId(id);
    }

}