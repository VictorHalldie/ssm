package com.itheima.ssm.dao;

import com.itheima.ssm.domain.SysLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ISysLogDao {


    @SelectKey(keyProperty = "sysLog.id", resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '')")
    @Options(keyProperty = "sysLog.id", useGeneratedKeys = true)
    @Insert("insert into syslog (id,visitTime,username,ip,url,executionTime,method) values(#{sysLog.id},#{sysLog.visitTime},#{sysLog.username},#{sysLog.ip},#{sysLog.url},#{sysLog.executionTime},#{sysLog.method})")
    public void save(@Param("sysLog") SysLog sysLog) throws  Exception;




    @Select("select * from sysLog")
    List<SysLog> findAll() throws  Exception;
}
