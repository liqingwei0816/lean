package com.guttv.mapper;

import com.guttv.bean.Rights;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RightsMapper {

    @Select("select * from t_rights")
    List<Rights> selectList();

}
