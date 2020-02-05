package com.guttv.mapper;

import com.github.pagehelper.PageHelper;
import com.guttv.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectList(@NotNull User user);

    default List<User> selectAll() {
        return selectList(new User());
    }


    User selectOne(@NotNull User user);

    default Long selectCount(@NotNull User user) {
        return PageHelper.count(() -> selectList(user));
    }

    @Delete("delete  from t_user where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull User user);

    Integer updateById(@NotNull User user);
}
