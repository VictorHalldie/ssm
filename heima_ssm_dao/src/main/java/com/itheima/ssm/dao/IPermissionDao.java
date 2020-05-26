package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Permission;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IPermissionDao {

    @Select("select * from permission where id in (select permissionId from role_permission where roleId=#{id})")
    public List<Permission> findPermissionByRoleId(String id) throws  Exception;



    @Select("select * from permission")
    public List<Permission> findAll() throws Exception;


    @SelectKey(keyProperty = "permission.id", resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '')")
    @Options(keyProperty = "permission.id", useGeneratedKeys = true)
    @Insert("insert into permission (id,permissionName,url) values (#{permission.id},#{permission.permissionName},#{permission.url})")
    void save(@Param("permission") Permission permission) throws Exception;
}


