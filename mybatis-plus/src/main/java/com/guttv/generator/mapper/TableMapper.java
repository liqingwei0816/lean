package com.guttv.generator.mapper;

import com.guttv.generator.entity.Field;
import com.guttv.generator.entity.Table;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {

    @Select("select table_name name,table_comment comment from information_schema.tables where table_schema=#{databaseName}")
    List<Table> getTables(String databaseName);

    @Select("select COLUMN_NAME name, DATA_TYPE type, COLUMN_COMMENT comment,CHARACTER_MAXIMUM_LENGTH length" +
            " FROM INFORMATION_SCHEMA.COLUMNS" +
            " where table_name = #{tableName}")
    List<Field> getFields(String tableName);


}
