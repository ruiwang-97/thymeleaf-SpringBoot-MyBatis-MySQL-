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
