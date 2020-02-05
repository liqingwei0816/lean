package com.guttv.service.impl;

import com.guttv.bean.User;
import com.guttv.mapper.UserMapper;
import com.guttv.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getList(User user) {
        return userMapper.selectList(user);
    }

    @Override
    public User getOne(User user) {
        return userMapper.selectOne(user);
    }

    @Override
    public Integer delete(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public Integer updateById(@NotNull User user) {
        return userMapper.updateById(user);
    }

}