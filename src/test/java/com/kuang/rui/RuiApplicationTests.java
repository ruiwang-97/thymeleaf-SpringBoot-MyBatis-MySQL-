package com.kuang.rui;

import com.kuang.rui.mapper.EmployeeMapper;
import com.kuang.rui.pojo.Department;
import com.kuang.rui.mapper.DepartmentMapper;
import com.kuang.rui.pojo.Employee;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.util.DateUtil.now;

@RunWith(SpringRunner.class)
@SpringBootTest
class RuiApplicationTests {

    @Autowired
    private DepartmentMapper departRepository;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Test
    void contextLoads() {
    }
    @Test
    void findDepart(){
        List<Department> all = departRepository.getDepartment();
        System.out.println(all);
    }

    @Test
    void findEmp(){
        List<Employee> all = employeeMapper.getAllEmp();
        System.out.println(all);
    }

    @Test
    void getEmplById(){
        Employee e = employeeMapper.getEmpById(1);
        System.out.println(e);
    }

    @Test
    @Transactional
    @Rollback
    void delete(){
        employeeMapper.deleteEmplById(1);
        List<Employee> all = employeeMapper.getAllEmp();
        System.out.println(all);
    }

    @Test
    @Transactional
    @Rollback
    void add(){
        //id;lastName;email;gender;did;departmentName;birth;
        employeeMapper.addEmpl(new Employee(5, "wr","2422@qq.com", 0, 102, "111", now()));
        List<Employee> all = employeeMapper.getAllEmp();
        System.out.println(all);
    }
}
