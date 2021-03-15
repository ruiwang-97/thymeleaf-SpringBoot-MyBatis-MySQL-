package com.kuang.rui.config;

import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/***
 * @Description: 自己定义的国际化解析器，根据类AcceptHeaderLocaleResolver实现接口
 * @Author: Wang Rui
 * @Date: $
 */
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
