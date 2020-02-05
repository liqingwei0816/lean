package com.guttv.mapper;

import com.github.pagehelper.PageHelper;
import com.guttv.bean.Rights;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface RightsMapper {

    List<Rights> selectList(@NotNull Rights rights);

    default List<Rights> selectAll() {
        return selectList(new Rights());
    }


    Rights selectOne(@NotNull Rights rights);

    default Long selectCount(@NotNull Rights rights) {
        return PageHelper.count(() -> selectList(rights));
    }

    @Delete("delete  from t_rights where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull Rights rights);

    Integer updateById(@NotNull Rights rights);
}
