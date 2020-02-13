package com.guttv.service;

import com.guttv.bean.RoleAuth;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RoleAuthService {

    List<RoleAuth> getList(RoleAuth roleAuth);

    RoleAuth getOne(RoleAuth roleAuth);

    Integer delete(Integer id);

    Integer insert(RoleAuth roleAuth);

    Integer updateById(@NotNull RoleAuth roleAuth);
}
