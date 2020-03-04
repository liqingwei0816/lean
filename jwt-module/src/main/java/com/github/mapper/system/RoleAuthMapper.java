package com.github.mapper.system;

import com.github.bean.system.RoleAuth;
import com.github.pagehelper.PageHelper;
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

    /**
     * 删除authId相关的所用绑定关系
     * @param authId 要删除的authId
     */
    @Delete("delete  from t_role_auth where authId = #{authId}")
    Integer deleteByAuthId(Integer authId);

    /**
     * 删除roleId相关的所用绑定关系
     * @param roleId 要删除的roleId
     */
    @Delete("delete  from t_role_auth where roleId = #{roleId}")
    Integer deleteByRoleId(Integer roleId);
}
