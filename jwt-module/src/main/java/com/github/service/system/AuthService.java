package com.github.service.system;


import com.github.bean.system.Auth;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface AuthService {

    List<Auth> getList(Auth auth);
    default List<Auth> getAll(){
        return getList(new Auth());
    }

    Auth getOne(Auth auth);

    Integer delete(@NotNull Integer id);

    Integer updateById(@NotNull Auth auth);

    Integer insert(Auth auth);

    Long count(Auth auth);

    /**
     * 根据用户名获取用户权限集合
     * @param userName 用户名
     */
    List<Auth> authoritiesByUsernameQuery(@NotNull String userName);

}
