package com.guttv.mapper;

import com.github.pagehelper.PageHelper;
import com.guttv.bean.system.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> selectList(@NotNull Role role);

    default List<Role> selectAll() {
        return selectList(new Role());
    }


    Role selectOne(@NotNull Role role);

    default Long selectCount(@NotNull Role role) {
        return PageHelper.count(() -> selectList(role));
    }

    @Delete("delete  from t_role where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull Role role);

    Integer updateById(@NotNull Role role);

    @Select("select authId from t_role_auth where roleId = #{roleId}")
    List<Integer> selectAuthByRole(Integer roleId);
}
