package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRoleDao {

    //根据用户的id查询出所有对应的角色
    @Select("select * from role where id in (select roleId from users_role where userId=#{userId})")
        @Results({
                @Result(id = true,property = "id" ,column = "id"),
                @Result(property = "roleName" ,column = "roleName"),
                @Result(property = "roleDesc" ,column = "roleDesc"),
                @Result(property = "permissions",column = "id", javaType = java.util.List.class,many = @Many(select = "com.itheima.ssm.dao.IPermissionDao.findPermissionByRoleId"))
        })
    public List<Role> findRoleByUser(String userId) throws Exception;


    @Select("select * from role")
    List<Role> findAll() throws Exception;


    @SelectKey(keyProperty = "role.id", resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '')")
    @Options(keyProperty = "role.id", useGeneratedKeys = true)
    @Insert("insert into role (id,roleName,roleDesc) values (#{role.id},#{role.roleName},#{role.roleDesc})")
    void save(@Param("role")Role role) throws  Exception;



    @Select("select * from role where id=#{roleId}")
        @Results({
                @Result(id = true,property = "id" ,column = "id"),
                @Result(property = "roleName" ,column = "roleName"),
                @Result(property = "roleDesc" ,column = "roleDesc"),
                @Result(property = "permissions" ,column = "id",javaType = java.util.List.class , many = @Many(select = "com.itheima.ssm.dao.IPermissionDao.findPermissionByRoleId")),
        })
    Role findById(String roleId) throws  Exception;



    @Select("select * from permission where id not in (select permissionId from role_permission where roleId=#{roleId})")
    List<Permission> findOtherPermissions(String roleId) throws  Exception;


    @Insert("insert into role_permission (permissionId,roleId) values (#{permissionId},#{roleId})")
    void addPermissionToRole(@Param("roleId") String roleId, @Param("permissionId") String permissionId) throws  Exception;
}
