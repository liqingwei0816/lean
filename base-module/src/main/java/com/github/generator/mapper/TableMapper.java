package com.github.generator.mapper;

import com.github.generator.entity.Field;
import com.github.generator.entity.Table;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TableMapper {

    @Select("select table_name `name`,table_comment `comment` from information_schema.tables where table_schema=#{databaseName}")
    List<Table> getTables(String databaseName);

    @Select("select COLUMN_NAME `name`, DATA_TYPE `type`, COLUMN_COMMENT `comment`,IFNULL(CHARACTER_MAXIMUM_LENGTH,0) + IFNULL(NUMERIC_PRECISION,0) `length`" +
            " FROM INFORMATION_SCHEMA.COLUMNS" +
            " where table_name = #{tableName} ORDER BY ORDINAL_POSITION")
    List<Field> getFields(String tableName);


}
