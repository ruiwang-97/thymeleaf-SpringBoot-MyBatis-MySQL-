package com.kuang.rui.service;

import com.kuang.rui.mapper.EmployeeMapper;
import com.kuang.rui.pojo.Employee;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.jws.WebParam;
import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
public interface EmployeeService {
    //获取全部员工
    String getAllE(Model model);
    //删除员工
    String deleteEmp(Model model, Integer id);
    //增加员工
    String toaddEmp(Model model);
    String addEmp(Employee employee);
    //通过id查询员工信息
    Employee getEmpById(Integer id);
//    修改员工信息
    String toupdateEmp(Integer id, Model model);
    String updateEmp(Employee employee);
}
