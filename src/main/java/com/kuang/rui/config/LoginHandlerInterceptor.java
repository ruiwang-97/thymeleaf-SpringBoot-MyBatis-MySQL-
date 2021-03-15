package com.kuang.rui.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * @Description: 登录拦截器
 * @Author: Wang Rui
 * @Date: $
 */
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
