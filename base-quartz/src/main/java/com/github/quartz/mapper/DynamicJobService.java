package com.github.quartz.mapper;

import com.github.controller.JobVo;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DynamicJobService {

    /**
     * quartz数据源
     */
    private DataSource quartzDataSource;

    public DynamicJobService(DataSource quartzDataSource) {
        this.quartzDataSource=quartzDataSource;
    }

    /**
     * 新增数据
     *
     * @param jobVo jobClassContent  jobClassByteCode  jobClass
     * @return 更新的数量
     */
    public Integer insert(JobVo jobVo) throws SQLException {
        String insert = "INSERT INTO `qrtz_dynamic_job`(`jobClass`, `jobClassContent`,`jobClassByteCode`) VALUES (?,?,?)";

        if (jobVo.getJobClassByteCode() == null) {
            insert = "INSERT INTO `qrtz_dynamic_job`(`jobClass`, `jobClassContent`) VALUES (?,?)";
        }
        try (Connection connection = quartzDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {

            preparedStatement.setString(1, jobVo.getJobClass());
            preparedStatement.setString(2, jobVo.getJobClassContent());
            if (jobVo.getJobClassByteCode() != null) {
                preparedStatement.setBytes(3, jobVo.getJobClassByteCode());
            }
            int count = preparedStatement.executeUpdate();
            log.debug("==>   Preparing: {}", insert);
            log.debug("==>  Parameters: {}(String),{}(String)", jobVo.getJobClass(), jobVo.getJobClassContent());
            log.debug("<==       Total: {}", count);
            return count;
        }
    }

    /**
     * 更新数据
     *
     * @param jobVo jobClassContent  jobClassByteCode  jobClass
     * @return 更新的数量
     */
    public Integer update(JobVo jobVo) throws SQLException {
        String update = "UPDATE `qrtz_dynamic_job` SET `jobClassContent`= ?,`jobClassByteCode`=? where `jobClass` =?";
        try (Connection connection = quartzDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, jobVo.getJobClassContent());
            preparedStatement.setBytes(2, jobVo.getJobClassByteCode());
            preparedStatement.setString(3, jobVo.getJobClass());

            int count = preparedStatement.executeUpdate();
            log.debug("==>   Preparing: {}", update);
            log.debug("==>  Parameters: {}(String),{}(String)", jobVo.getJobClassContent(), jobVo.getJobClass());
            log.debug("<==       Total: {}", count);
            return count;
        }
    }

    /**
     * 获取源代码数据
     *
     * @param jobClass 类名
     * @return 源代码
     */
    public String selectContentByClassName(String jobClass) {

        String select = "select `jobClassContent`  from `qrtz_dynamic_job` where jobClass= ?";
        try (Connection connection = quartzDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select)) {

            preparedStatement.setString(1, jobClass);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("jobClassContent");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取字节码数据
     *
     * @param jobClass 类名
     */
    public byte[] selectByteCodeByClassName(String jobClass) {

        String select = "select `jobClassByteCode`  from `qrtz_dynamic_job` where jobClass= ?";
        try (Connection connection = quartzDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select)) {
            preparedStatement.setString(1, jobClass);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBytes("jobClassByteCode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


}
