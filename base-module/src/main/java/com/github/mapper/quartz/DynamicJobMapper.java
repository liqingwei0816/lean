package com.github.mapper.quartz;

import com.github.controller.quartz.JobVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DynamicJobMapper {

    @Insert("INSERT INTO `qrtz_dynamic_job`(`jobClass`, `jobClassContent`) VALUES (#{jobClass}, #{jobClassContent})")
    Integer insert(JobVo jobVo);

    @Select("select `jobClassContent`  from `qrtz_dynamic_job` where jobClass= #{jobClass}")
    String selectByClassName(String jobClass);


}
