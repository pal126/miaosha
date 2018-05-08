package com.pal.miaosha.dao;

import com.pal.miaosha.domain.Usera;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDaoa {

    @Select("select * from user where id = #{id}")
    Usera getById(@Param("id") int id);

}
