package com.guttv.service;

import com.guttv.bean.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {

    List<User> getList(User user);

    User getOne(User user);

    Integer delete(Integer id);

    Integer insert(User user);

    Integer updateById(@NotNull User user);
}
