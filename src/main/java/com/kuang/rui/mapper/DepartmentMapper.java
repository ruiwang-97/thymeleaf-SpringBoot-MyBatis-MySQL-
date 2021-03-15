package com.kuang.rui.mapper;

import com.kuang.rui.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description: @Mapper注解，能够扫描mapper接口，生成的代理对象
 * @Author: Wang Rui
 * @Date: $
 */
@Mapper
@Repository
public interface DepartmentMapper {
    //获得所有部门信息
    List<Department> getDepartment();

    //通过id获得部门
     List<Department> getDepartmentById(int id);
}
