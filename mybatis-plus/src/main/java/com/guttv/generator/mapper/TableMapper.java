package com.guttv.generator.mapper;

import com.guttv.generator.entity.Field;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {

    @Select("show tables")
    List<Map<String,Object>> getTables();
    @Select("select COLUMN_NAME name, DATA_TYPE type, COLUMN_COMMENT comment,CHARACTER_MAXIMUM_LENGTH length" +
            " FROM INFORMATION_SCHEMA.COLUMNS" +
            " where table_name = #{tableName}")
    List<Field> getFields(String tableName);


}
