package com.kuang.rui.controller;

import com.kuang.rui.mapper.DepartmentMapper;
import com.kuang.rui.mapper.EmployeeMapper;
import com.kuang.rui.pojo.Department;
import com.kuang.rui.pojo.Employee;
import com.kuang.rui.service.DepartmentService;
import com.kuang.rui.service.EmployeeService;
import com.kuang.rui.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    DepartmentService departmentService;

    @RequestMapping("/emps")
    public String list(Model model){
        return employeeService.getAllE(model);
    }

    @GetMapping("/emp/delete/{id}")
    public String delete(Model model, @PathVariable(name = "id") Integer id){
        return employeeService.deleteEmp(model, id);
    }

    @GetMapping("/emp")
    public String toAdd(Model model) {
        return employeeService.toaddEmp(model);
    }

    @PostMapping("/emp")
    public String add(Employee employee){
        return employeeService.addEmp(employee);
    }

    @GetMapping("/emp/update/{id}")
    public String toupdataEmp(@PathVariable("id") int id, Model model) {
        return employeeService.toupdateEmp(id, model);
    }
    @PostMapping("/emp/update")
    public String updataEmp(Employee employee) {
        return employeeService.updateEmp(employee);
    }
}
