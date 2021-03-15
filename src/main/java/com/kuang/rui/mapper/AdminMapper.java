package com.kuang.rui.mapper;

import com.kuang.rui.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
@Mapper
public interface AdminMapper {
    Admin login(@Param("username")String username,@Param("password") String password);
}
