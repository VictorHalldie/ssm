package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUserDao {

    @Select("select * from users where username=#{username}")
    @Results({
            @Result(id = true,property = "id" ,column = "id"),
            @Result(property = "username" ,column = "username"),
            @Result(property = "email" ,column = "email"),
            @Result(property = "password" ,column = "password"),
            @Result(property = "phoneNum" ,column = "phoneNum"),
            @Result(property = "status" ,column = "status"),
            @Result(property = "roles" ,column = "id",javaType = java.util.List.class ,many = @Many(select = "com.itheima.ssm.dao.IRoleDao.findRoleByUser"))


    })
    public UserInfo findByUsername(String username) throws  Exception;


    @Select("select * from users ")
    List<UserInfo> findAll() throws Exception;



    @SelectKey(keyProperty = "userInfo.id", resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '')")
    @Options(keyProperty = "userInfo.id", useGeneratedKeys = true)
    @Insert("insert into users(id,email,username,password,phoneNum,status) values (#{userInfo.id},#{userInfo.email},#{userInfo.username},#{userInfo.password},#{userInfo.phoneNum},#{userInfo.status}) ")
    void save(@Param("userInfo")UserInfo userInfo) throws Exception;



    @Select("select * from users where id = #{id} ")
     @Results({
             @Result(id = true,property = "id" ,column = "id"),
             @Result(property = "email" ,column = "email"),
             @Result(property = "username" ,column = "username"),
             @Result(property = "password" ,column = "password"),
             @Result(property = "phoneNum" ,column = "phoneNum"),
             @Result(property = "status" ,column = "status"),
             @Result(property = "roles" ,column = "id",javaType = java.util.List.class ,many = @Many(select = "com.itheima.ssm.dao.IRoleDao.findRoleByUser"))
     })
    UserInfo findById(String id) throws  Exception;



    @Select("select * from role where id not in (select roleId from users_role where userId =#{userId})")
    List<Role> findOtherRole(String userId) throws  Exception;


    @Insert("insert into users_role (userId,roleId) values (#{userId},#{roleId})")
    void addRoleToUser(@Param("userId") String userId,@Param("roleId") String roleId);
}
