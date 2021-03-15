package com.kuang.rui.mapper;

import com.kuang.rui.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
@Mapper
@Repository
public interface EmployeeMapper {
    //查询全部员工信息
    List<Employee> getAllEmp();
    //删除员工
    int deleteEmplById(@Param("id") Integer id);
    //添加员工
    int addEmpl(Employee employee);
    //通过id查询员工
    Employee getEmpById(Integer id);
//    修改员工
    int updateEmpl(Employee employee);



}
