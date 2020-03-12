package com.github.mapper.system;

import com.github.pagehelper.PageHelper;
import com.github.bean.system.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface SysUserRoleMapper {

    List<SysUserRole> selectList(@NotNull SysUserRole sysUserRole);

    default List<SysUserRole> selectAll() {
        return selectList(new SysUserRole());
    }


    SysUserRole selectOne(@NotNull SysUserRole sysUserRole);

    default Long selectCount(@NotNull SysUserRole sysUserRole) {
        return PageHelper.count(() -> selectList(sysUserRole));
    }

    @Delete("delete  from t_sys_user_role where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull SysUserRole sysUserRole);

    Integer updateById(@NotNull SysUserRole sysUserRole);

    Integer insertNoExists(SysUserRole sysUserRole);

    @Delete("delete  from t_sys_user_role where sysUserId = #{sysUserId}")
    void deleteBySysUserId(Integer sysUserId);
}
