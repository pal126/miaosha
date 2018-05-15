package com.pal.miaosha.dao;

import com.pal.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(@Param("id") Long id);

    @Update("update user set password = #{password} where id = #{id}")
    int update(User user);

}
