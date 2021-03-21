## 项目简介
在[狂神的SpringBoot员工管理项目的基础](https://blog.csdn.net/qq_39181839/article/details/111304598)上，整合了MySQL和MyBatis。
新建管理员表、员工表和部门表，进行Mapper层、Service层和Controller层的重新编写，实现了管理员登录及员工的增删改查等功能。
## 项目源码
[百度网盘](https://pan.baidu.com/s/12Rq41luttRM8aTbiLVZrQg)  提取密码：wrbg
### 环境要求
- IDEA 
- MySQL
- Maven
- 掌握MySQL数据库，SpringBoot及MyBatis知识，简单的前端知识；
### 数据库环境
需求分析：
- 管理员登录，对员工进行管理，建立admin表
- 对员工进行增删改查，肯定有员工表employ
- 员工的部门department表
```SQL
create database employee;

drop table if exists department;

create table department
(
    id    int primary key,
    departmentName varchar(20) not null
);

drop table if exists employ;

create table employ
(
    id       int primary key,
    lastName    varchar(50) not null,
    email    varchar(50),
    gender   int,
    birthday datetime,
    did      int references department (id)
);

drop table if exists admin;

create table admin
(
    id       int primary key,
	username    varchar(50) not null,
	password    varchar(50) not null
);

insert into department
values (101, '教学部'),
       (102, '市场部'),
       (103, '教研部');

insert into employ
VALUES ('王珊', 'wang4567@qq.com', 1,  101, '1998-02-12'),
       ('章程', 'zhang7@163.com', 0, 102, '1990-01-12'),
       ('李旺', 'liwang4335@qq.com', 1,103, '1997-02-18');

insert into admin
VALUES (1,'admin','123');
```
### 基本环境搭建
1. Spring Initializr 快速创建一个项目， 添加Lombok，Spring Web，Thymeleaf，Mybatis，MySQL Driver的依赖
2. pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.8.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.kuang</groupId>
    <artifactId>rui</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>rui</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.0</version>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <!-- 由于commons-fileupload组件不属于Spring Boot，所以需要加上版本 -->
            <version>1.3.3</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- 单元测试 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>RELEASE</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

3. 建立层级结构
 config：存放配置类
pojo：实体类，类的属性要与表的字段一致
mapper：数据持久层，负责java和数据库交互
service：处理业务逻辑
controller：处理前端请求
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210321152141382.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5MTgxODM5,size_16,color_FFFFFF,t_70)
4. application.properties里配置数据库连接信息、Mapper映射文件信息及其他信息
```
#关闭模板引擎缓存
spring.thymeleaf.cache=false

server.servlet.context-path=/kuang

#配置文件放置位置
spring.messages.basename=i18n/login

##时间日期格式化（与前端匹配）
#spring.mvc.date-format=yyyy-MM-dd
#spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
#spring.jackson.time-zone=Hongkong
#数据源信息配置
spring.datasource.url=jdbc:mysql://localhost:3306/employee?useUnicode=true&characterEncoding=utf-8&&useSSL=false&serverTimezone=Hongkong&&allowPublicKeyRetrieval=true
#数据库用户名
spring.datasource.username=root
#数据库密码
spring.datasource.password=root
#数据库驱动
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#整合mybatis
#设置包别名（在Mapper映射文件中直接使用实体类名）
mybatis.type-aliases-package=com.kuang.rui.pojo
#告诉系统在哪里去找mapper.xml文件（映射文件）
mybatis.mapperLocations=classpath:mappers/*.xml
#在控制台输出SQL语句日志
logging.level.com.ch.ebusiness.repository=debug
#上传文件时，默认单个上传文件大小是1MB，max-file-size设置单个上传文件大小
spring.servlet.multipart.max-file-size=50MB
#默认总文件大小是10MB，max-request-size设置总上传文件大小
spring.servlet.multipart.max-request-size=500MB
```
在启动类中加上 @MapperScan(value = {"com.kuang.rui.mapper"}) 来扫描该目录。
```java
@SpringBootApplication
@MapperScan(value = {"com.kuang.rui.mapper"})
public class RuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuiApplication.class, args);
    }
}
 ```

### 创建pojo实体类
#### Admin类
```java
public class Admin {
    private String username;
    private String password;
}
```

#### Department类
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private Integer id;
    private String departmentName;
}
```
#### Employee类
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private Integer gender;//0:女 1：男
    private Integer did;
    private String departmentName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="Hongkong")
    private Date birth;
}
```
### Mapper层

文件存放目录：

- com.kuang.rui.mapper 相关接口及实现类

- resources/mappers 相关mapper.xml

#### Admin 

1. 编写Admin的Mapper接口：AdminMapper
```java
@Mapper
public interface AdminMapper {
    Admin login(@Param("username")String username,@Param("password") String password);
}
```
2. 编写接口对应的Mapper.xml文件：AdminMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.kuang.rui.mapper.AdminMapper">
    <!--Select查询语句-->
    <select id="login" resultType="com.kuang.rui.pojo.Admin">
		select * from admin where username=#{username} and password=#{password}
	</select>

</mapper>
```
#### Department
1. 编写Department的Mapper接口：DepaertmentMapper
```java
@Mapper
@Repository
public interface DepartmentMapper {
    //获得所有部门信息
    List<Department> getDepartment();

    //通过id获得部门
     List<Department> getDepartmentById(int id);
}
```
2. 编写接口对应的Mapper.xml文件：DepartmentMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.kuang.rui.mapper.DepartmentMapper">
    <!--Select查询语句-->
    <select id="getDepartment"  resultType="com.kuang.rui.pojo.Department">
		select * from department
	</select>

</mapper>
```
#### Employee
1. 编写Employee的Mapper接口：EmployeeMapper
```java
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
```
2. 编写接口对应的Mapper.xml文件：EmployeeMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.kuang.rui.mapper.EmployeeMapper">
    <!--Select查询语句-->
    <select id="getAllEmp"  resultType="com.kuang.rui.pojo.Employee">
		select e.id, e.lastName, e.email, e.gender, e.birth, e.did, departmentName
        from employ e
        left join department d on d.id = e.did
	</select>
    <!--删除员工信息-->
    <delete id="deleteEmplById">
        delete from employ e where e.id=#{id}
    </delete>
    <!--添加员工-->
    <insert id="addEmpl" parameterType="com.kuang.rui.pojo.Employee">
        insert into employ (id,lastName,email,gender,did,birth)
         values (#{id},#{lastName},#{email},#{gender},#{did},#{birth})
    </insert>
    <!--通过id查询员工信息-->
    <select id="getEmpById" resultType="com.kuang.rui.pojo.Employee">
        select e.id, e.lastName, e.email, e.gender, e.birth, e.did, departmentName
        from employ e
        left join department d on d.id = e.did
        where e.id = #{id}
    </select>
    <!--修改员工信息-->
    <update id="updateEmpl" parameterType="com.kuang.rui.pojo.Employee">
        update employ set lastName=#{lastName},
                          email=#{email},
                          gender=#{gender},
                          did=#{did},
                          birth=#{birth} where id=#{id}
    </update>
</mapper>
```
### Service层
#### AdminService
1. 接口
```java
public interface AdminService {
    String adminLogin(String username, String password, Model model, HttpSession httpSession);
}
```
2. 具体实现类
```java
package com.kuang.rui.service;

import com.kuang.rui.mapper.AdminMapper;
import com.kuang.rui.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminMapper adminMapper;


//    public Admin selectPasswordByName(String username, String password) {
//        return adminMapper.login(username,password);
//    }
    @Override
    public String adminLogin(String username, String password, Model model, HttpSession httpSession){
        Admin admin = adminMapper.login(username, password);
        if(admin != null){
//            return "dashboard";
            httpSession.setAttribute("loginUser", username);//拦截器获取此session
            //重定向
            return "redirect:/main.html";

        }else{
            //消息回显，需要model
            model.addAttribute("msg","用户名或密码错误");
            return "index";
        }
    }
}
````
#### DepartmentService
1. 接口
```java
public interface DepartmentService {
    //查询全部员工信息
    List<Department> getAllD();
}
```
2. 具体实现类
```java
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
```
#### EmployeeService
1. 接口
```java
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
```
2. 具体实现类
```java
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
```
### Controller层
#### 登录 AdminController
```java
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession httpSession){
        return adminService.adminLogin(username, password, model, httpSession);
    }
    @RequestMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }

}
````
#### 员工管理 EmployeeController
```java
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
```
### Config
#### 登录拦截器配置
```java
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录成功之后应该有用户的session
        Object userSession = request.getSession().getAttribute("loginUser");
        //userSession为空，说明没有登录，需要拦截
        if(userSession == null){
            //告诉一个消息
            request.setAttribute("msg", "没有权限， 请先登录！");
            //再返回到首页
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        }else{
            return true;
        }
    }
}
```
#### 国际化解析器
```java
public class MyLocalResolver implements LocaleResolver {

    //解析请求
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        //如果获取请求中的语言参数链接，就构造一个自己的
        String language = request.getParameter("l");//l对应着index.html文件中的跳转链接

//        System.out.println(language);
        //如果没有就使用默认
        Locale locale = Locale.getDefault();
        //如果请求的链接携带了国际化参数，就构造一个自己的
        if(!StringUtils.isEmpty(language)){
            String[] s = language.split("_");//zh_CN
            //国家、地区
             locale = new Locale(s[0], s[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
```
#### 编写Mvc文件，将上述配置到MvcConfiguration中
```java
@Configuration
//@EnableWebMvc
public class MyMvcConfig implements WebMvcConfigurer {
    //添加视图映射
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");

        //登录功能实现完毕，但是浏览器的url暴露了用户的用户名和密码，因此我们需要编写一个映射
        //将dashboard映射到main.html
        registry.addViewController("/main.html").setViewName("dashboard");

    }

    //向容器中注入自定义的国际化组件才能生效
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocalResolver();
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/","/index.html","/user/login","/css/**","/img/**","/js/**");

    }
}
```
### 前端视图
在上一项目中修改了涉及 `did ` 的html文件
#### 登录页index.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Signin Template for Bootstrap</title>
    <!-- Bootstrap core CSS -->
    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link th:href="@{/css/signin.css}" rel="stylesheet">
</head>

<body class="text-center">
<form class="form-signin" th:action="@{/user/login}">
    <img class="mb-4" th:src="@{/img/bootstrap-solid.svg}" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal" th:text="#{login.tip}">Please sign in</h1>
    <!--msg不为空才显示-->
    <p style="color:red" th:text="${msg}" th:if="${not #strings.isEmpty(msg)}"></p>


    <input type="text" name="username" class="form-control" th:placeholder="#{login.username}" required="" autofocus="">
    <input type="password"  name="password" class="form-control" th:placeholder="#{login.password}" required="">
    <div class="checkbox mb-3">
        <label>
            <input type="checkbox" value="remember-me"> [[#{login.remember}]]
        </label>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit">[[#{login.btn}]]</button>
    <p class="mt-5 mb-3 text-muted">© 2017-2018</p>
    <a class="btn btn-sm" th:href="@{/(l='zh_CN')}">中文</a>
    <a class="btn btn-sm" th:href="@{/(l='en_US')}">English</a>
</form>

</body>

</html>
```
#### 首页dashboard.html
```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Dashboard Template for Bootstrap</title>
    <!-- Bootstrap core CSS -->
    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link th:href="@{/css/dashboard.css}" rel="stylesheet">
    <style type="text/css">
        /* Chart.js */

        @-webkit-keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        @keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        .chartjs-render-monitor {
            -webkit-animation: chartjs-render-animation 0.001s;
            animation: chartjs-render-animation 0.001s;
        }
    </style>
</head>

<body>
<!--利用th:replace="~{}"标签引入抽取之后的-->
<!--导航栏-->
<div th:replace="~{common/common::topbar}"></div>
<div class="container-fluid">
    <div class="row">
        <!--侧边栏-->
<!--传递参数给组件-->
        <div th:replace="~{common/common::sidebar(active='main.html')}"></div>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
            <div class="chartjs-size-monitor"
                 style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;">
                <div class="chartjs-size-monitor-expand"
                     style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;">
                    <div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div>
                </div>
                <div class="chartjs-size-monitor-shrink"
                     style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;">
                    <div style="position:absolute;width:200%;height:200%;left:0; top:0"></div>
                </div>
            </div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">Dashboard</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <div class="btn-group mr-2">
                        <button class="btn btn-sm btn-outline-secondary">Share</button>
                        <button class="btn btn-sm btn-outline-secondary">Export</button>
                    </div>
                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                             stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                             class="feather feather-calendar">
                            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                            <line x1="16" y1="2" x2="16" y2="6"></line>
                            <line x1="8" y1="2" x2="8" y2="6"></line>
                            <line x1="3" y1="10" x2="21" y2="10"></line>
                        </svg>
                        This week
                    </button>
                </div>
            </div>

            <canvas class="my-4 chartjs-render-monitor" id="myChart" width="1076" height="454"
                    style="display: block; width: 1076px; height: 454px;"></canvas>


        </main>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js"></script>
<script type="text/javascript" src="asserts/js/popper.min.js"></script>
<script type="text/javascript" src="asserts/js/bootstrap.min.js"></script>

<!-- Icons -->
<script type="text/javascript" src="asserts/js/feather.min.js"></script>
<script>
    feather.replace()
</script>

<!-- Graphs -->
<script type="text/javascript" src="asserts/js/Chart.min.js"></script>
<script>
    var ctx = document.getElementById("myChart");
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            datasets: [{
                data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#007bff',
                borderWidth: 4,
                pointBackgroundColor: '#007bff'
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false,
            }
        }
    });
</script>

</body>

</html>
```
#### 公共页面common.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<!--th:fragment标签抽取公共部分-->
<!--导航栏-->
<nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0" th:fragment="topbar">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">admin</a>
    <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
    <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
            <a class="nav-link" th:href="@{/user/logout}">注销</a>
        </li>
    </ul>
</nav>
<!--侧边栏-->
<nav class="col-md-2 d-none d-md-block bg-light sidebar" th:fragment="sidebar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a th:class="${active=='main.html'?'nav-link active':'nav-link'}" th:href="@{/index.html}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-home">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                        <polyline points="9 22 9 12 15 12 15 22"></polyline>
                    </svg>
                    首页 <span class="sr-only">(current)</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file">
                        <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"></path>
                        <polyline points="13 2 13 9 20 9"></polyline>
                    </svg>
                    Orders
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-shopping-cart">
                        <circle cx="9" cy="21" r="1"></circle>
                        <circle cx="20" cy="21" r="1"></circle>
                        <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                    </svg>
                    Products
                </a>
            </li>
            <li class="nav-item">
                <a th:class="${active=='list.html'?'nav-link active':'nav-link'}" th:href="@{/emps}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-users">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                    用户管理
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-bar-chart-2">
                        <line x1="18" y1="20" x2="18" y2="10"></line>
                        <line x1="12" y1="20" x2="12" y2="4"></line>
                        <line x1="6" y1="20" x2="6" y2="14"></line>
                    </svg>
                    Reports
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-layers">
                        <polygon points="12 2 2 7 12 12 22 7 12 2"></polygon>
                        <polyline points="2 17 12 22 22 17"></polyline>
                        <polyline points="2 12 12 17 22 12"></polyline>
                    </svg>
                    Integrations
                </a>
            </li>
        </ul>

        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Saved reports</span>
            <a class="d-flex align-items-center text-muted"
               href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="feather feather-plus-circle">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="16"></line>
                    <line x1="8" y1="12" x2="16" y2="12"></line>
                </svg>
            </a>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Current month
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Last quarter
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Social engagement
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Year-end sale
                </a>
            </li>
        </ul>
    </div>
</nav>
</body>
</html>
```
### 员工列表list.html
```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Dashboard Template for Bootstrap</title>
    <!-- Bootstrap core CSS -->
    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link th:href="@{/css/dashboard.css}" rel="stylesheet">
    <style type="text/css">
        /* Chart.js */

        @-webkit-keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        @keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        .chartjs-render-monitor {
            -webkit-animation: chartjs-render-animation 0.001s;
            animation: chartjs-render-animation 0.001s;
        }
    </style>
</head>

<body>
<!--导航栏-->
<div th:replace="~{common/common::topbar}"></div>

<div class="container-fluid">
    <div class="row">
        <!--侧边栏-->
        <div th:replace="~{common/common::sidebar(active='list.html')}"></div>
        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
            <h2><a class="btn btn-sm btn-success" th:href="@{/emp}">添加员工</a></h2>
            <div class="table-responsive">

                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th>编号</th>
                        <th>姓名</th>
                        <th>邮箱</th>
                        <th>性别</th>
                        <th>部门</th>
                        <th>出生日期</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <!--从model中的emps取存入em中-->
                        <tr th:each="em:${empl}">
                            <td th:text="${em.getId()}"></td>
                            <td th:text="${em.getLastName()}"></td>
                            <td th:text="${em.getEmail()}"></td>
                            <td th:text="${em.getGender()==0?'女':'男'}"></td>
                            <td th:text="${em.getDepartmentName()}"></td>
                            <td th:text="${#dates.format(em.getBirth(),'yyyy-MM-dd')}"></td>
                            <td> <a class="btn btn-sm btn-primary" th:href="@{/emp/update/}+${em.id}">编辑</a>
                                <a class="btn btn-sm btn-danger" th:href="@{/emp/delete/}+${em.id}">删除</a>

                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

        </main>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js"></script>
<script type="text/javascript" src="asserts/js/popper.min.js"></script>
<script type="text/javascript" src="asserts/js/bootstrap.min.js"></script>

<!-- Icons -->
<script type="text/javascript" src="asserts/js/feather.min.js"></script>
<script>
    feather.replace()
</script>

<!-- Graphs -->
<script type="text/javascript" src="asserts/js/Chart.min.js"></script>
<script>
    var ctx = document.getElementById("myChart");
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            datasets: [{
                data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#007bff',
                borderWidth: 4,
                pointBackgroundColor: '#007bff'
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false,
            }
        }
    });
</script>

</body>

</html>
```
#### 添加员工addEmp.html
```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Dashboard Template for Bootstrap</title>
    <!-- Bootstrap core CSS -->
    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link th:href="@{/css/dashboard.css}" rel="stylesheet">
    <style type="text/css">
        /* Chart.js */

        @-webkit-keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        @keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        .chartjs-render-monitor {
            -webkit-animation: chartjs-render-animation 0.001s;
            animation: chartjs-render-animation 0.001s;
        }
    </style>
</head>

<body>
<!--导航栏-->
<div th:replace="~{common/common::topbar}"></div>

<div class="container-fluid">
    <div class="row">
        <!--侧边栏-->
        <div th:replace="~{common/common::sidebar(active='list.html')}"></div>
        <p style="color:red" th:text="${Message}" th:if="${not #strings.isEmpty(Message)}"></p>
        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
            <!--提交表单，点击添加按钮跳转-->
            <form th:action="@{/emp}" method="post">

                <div class="form-group">
                    <label>编号</label>
                    <input type="text" name="id" class="form-control" placeholder="1">
                </div>
                <div class="form-group">
                    <label>姓名</label>
                    <input type="text" name="lastName" class="form-control" placeholder="zhangsan">
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" name="email" class="form-control" placeholder="zhangsan@atguigu.com">
                </div>
                <div class="form-group">
                    <label>性别</label><br/>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="gender"  value="1">
                        <label class="form-check-label">男</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="gender"  value="0">
                        <label class="form-check-label">女</label>
                    </div>
                </div>
                <div class="form-group">
                    <label>部门</label>
                <!--这里传入参数是id，与value对应。在controller接收的是一个Employee对象，所以需要提交的是其中的一个属性-->
                    <select class="form-control" name="did">
                <!--th:text 显示为部门名 用户接收的是部门id，value为部门id-->
                        <option th:each="dep:${departments}" th:text="${dep.getDepartmentName()}" th:value="${dep.getId()}"></option>
                    </select>
                </div>
                <div class="form-group">
                    <label>出生日期</label>
                    <input type="text" name="birth" class="form-control" placeholder="zhangsan">
                </div>
                <button type="submit" class="btn btn-primary">添加</button>
            </form>
        </main>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js"></script>
<script type="text/javascript" src="asserts/js/popper.min.js"></script>
<script type="text/javascript" src="asserts/js/bootstrap.min.js"></script>

<!-- Icons -->
<script type="text/javascript" src="asserts/js/feather.min.js"></script>
<script>
    feather.replace()
</script>

<!-- Graphs -->
<script type="text/javascript" src="asserts/js/Chart.min.js"></script>
<script>
    var ctx = document.getElementById("myChart");
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            datasets: [{
                data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#007bff',
                borderWidth: 4,
                pointBackgroundColor: '#007bff'
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false,
            }
        }
    });
</script>

</body>

</html>
```
#### 修改员工update.html
```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Dashboard Template for Bootstrap</title>
    <!-- Bootstrap core CSS -->
    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link th:href="@{/css/dashboard.css}" rel="stylesheet">
    <style type="text/css">
        /* Chart.js */

        @-webkit-keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        @keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        .chartjs-render-monitor {
            -webkit-animation: chartjs-render-animation 0.001s;
            animation: chartjs-render-animation 0.001s;
        }
    </style>
</head>

<body>
<!--导航栏-->
<div th:replace="~{common/common::topbar}"></div>

<div class="container-fluid">
    <div class="row">
        <!--侧边栏-->
        <div th:replace="~{common/common::sidebar(active='list.html')}"></div>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
            <!--提交表单，点击添加按钮跳转-->
            <form th:action="@{/emp/update}" method="post">
                <!--指定修改人的id-->
                <input type="hidden" name="id" th:value="${emp.getId()}">

                <div class="form-group">
                    <label>姓名</label>
                    <input type="text" name="lastName" class="form-control" placeholder="zhangsan" th:value="${emp.getLastName()}">
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" name="email" class="form-control" placeholder="zhangsan@atguigu.com"  th:value="${emp.getEmail()}">
                </div>
                <div class="form-group">
                    <label>性别</label><br/>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="gender"  value="1" th:checked="${emp.getGender()==1}">
                        <label class="form-check-label">男</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="gender"  value="0" th:checked="${emp.getGender()==0}">
                        <label class="form-check-label">女</label>
                    </div>
                </div>
                <div class="form-group">
                    <label>部门</label>
                <!--这里传入参数是id，与value对应。在controller接收的是一个Employee对象，所以需要提交的是其中的一个属性-->
                    <select class="form-control" name="did">
                <!--th:text 显示为部门名 用户接收的是部门id，value为部门id-->
                        <option th:selected="${emp.getDid()==dep.getId()}" th:each="dep:${departments}" th:text="${dep.getDepartmentName()}" th:value="${dep.getId()}"></option>
                    </select>
                </div>
                <div class="form-group">
                    <label>出生日期</label>
                    <input type="text" name="birth" class="form-control" placeholder="zhangsan"  th:value="${#dates.format(emp.getBirth(),'yyyy-MM-dd')}">
                </div>
                <button type="submit" class="btn btn-primary">修改</button>
            </form>
        </main>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js"></script>
<script type="text/javascript" src="asserts/js/popper.min.js"></script>
<script type="text/javascript" src="asserts/js/bootstrap.min.js"></script>

<!-- Icons -->
<script type="text/javascript" src="asserts/js/feather.min.js"></script>
<script>
    feather.replace()
</script>

<!-- Graphs -->
<script type="text/javascript" src="asserts/js/Chart.min.js"></script>
<script>
    var ctx = document.getElementById("myChart");
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            datasets: [{
                data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#007bff',
                borderWidth: 4,
                pointBackgroundColor: '#007bff'
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false,
            }
        }
    });
</script>

</body>

</html>
```
