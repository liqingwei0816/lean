package com.github.quartz.mapper.system;

import com.github.pagehelper.PageHelper;
import com.github.bean.system.Auth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface AuthMapper {

    List<Auth> selectList(@NotNull Auth auth);

    @Select("SELECT * " +
            "FROM t_auth " +
            "WHERE " +
            "id IN (SELECT authId FROM t_role_auth WHERE roleId IN ( SELECT roleId FROM t_sys_user_role WHERE sysUserId =( SELECT id FROM t_sys_user WHERE userName = #{userName})))")
    List<Auth> authoritiesByUsernameQuery(@NotNull String userName);

    default List<Auth> selectAll() {
        return selectList(new Auth());
    }


    Auth selectOne(@NotNull Auth auth);

    default Long selectCount(@NotNull Auth auth) {
        return PageHelper.count(() -> selectList(auth));
    }

    @Delete("delete  from t_auth where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull Auth auth);

    Integer updateById(@NotNull Auth auth);
}
