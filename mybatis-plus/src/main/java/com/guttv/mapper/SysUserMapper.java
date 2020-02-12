package com.guttv.mapper;

import com.github.pagehelper.PageHelper;
import com.guttv.bean.SysUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface SysUserMapper {

    List<SysUser> selectList(@NotNull SysUser sysUser);

    default List<SysUser> selectAll() {
        return selectList(new SysUser());
    }


    SysUser selectOne(@NotNull SysUser sysUser);

    default Long selectCount(@NotNull SysUser sysUser) {
        return PageHelper.count(() -> selectList(sysUser));
    }

    @Delete("delete  from t_sys_user where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull SysUser sysUser);

    Integer updateById(@NotNull SysUser sysUser);
}
