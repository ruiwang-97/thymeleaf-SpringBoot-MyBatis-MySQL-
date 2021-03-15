package com.kuang.rui.service;

import com.kuang.rui.mapper.DepartmentMapper;
import com.kuang.rui.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
@Service
public class DepartmentServiceImpl implements DepartmentService{
    @Autowired
    DepartmentMapper departmentMapper;
    //查询全部员工信息
    @Override
    public List<Department> getAllD(){
        return departmentMapper.getDepartment();
    }

}
