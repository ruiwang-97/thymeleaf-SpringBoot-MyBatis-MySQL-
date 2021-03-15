package com.kuang.rui.service;

import com.kuang.rui.mapper.DepartmentMapper;
import com.kuang.rui.mapper.EmployeeMapper;
import com.kuang.rui.pojo.Department;
import com.kuang.rui.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    @Override
//    public List<Employee> getAllE(){
//        return employeeMapper.getAllEmp();
//    }
    public String getAllE(Model model) {
        List<Employee> employees = employeeMapper.getAllEmp();
        model.addAttribute("empl", employees);
        return "emp/list";
    }

    @Override
    public String deleteEmp(Model model, Integer id){
         employeeMapper.deleteEmplById(id);
         return "redirect:/emps";
    }

    @Override
    public String toaddEmp(Model model){
        //查出部门的所有信息
        List<Department> departments = departmentMapper.getDepartment();
        model.addAttribute("departments", departments);
        return "emp/addEmp";
    }
    @Override
    public String addEmp(Employee employee) {
        employeeMapper.addEmpl(employee);
        return "redirect:/emps";
    }

    @Override
    public Employee getEmpById(Integer id) {
        return employeeMapper.getEmpById(id);
    }


    @Override
    public String toupdateEmp(Integer id, Model model){
        Employee employee = employeeMapper.getEmpById(id);
        model.addAttribute("emp", employee);

        List<Department> departments = departmentMapper.getDepartment();
        model.addAttribute("departments", departments);

        return "emp/update";
    }
    @Override
    public String updateEmp(Employee employee){
        employeeMapper.updateEmpl(employee);
        return "redirect:/emps";
    }


}
