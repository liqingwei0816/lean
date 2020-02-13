package com.guttv.mapper;

import com.github.pagehelper.PageHelper;
import com.guttv.bean.RoleAuth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface RoleAuthMapper {

    List<RoleAuth> selectList(@NotNull RoleAuth roleAuth);

    default List<RoleAuth> selectAll() {
        return selectList(new RoleAuth());
    }


    RoleAuth selectOne(@NotNull RoleAuth roleAuth);

    default Long selectCount(@NotNull RoleAuth roleAuth) {
        return PageHelper.count(() -> selectList(roleAuth));
    }

    @Delete("delete  from t_role_auth where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull RoleAuth roleAuth);

    Integer updateById(@NotNull RoleAuth roleAuth);
}
