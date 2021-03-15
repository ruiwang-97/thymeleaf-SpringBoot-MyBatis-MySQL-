package com.kuang.rui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * @Description:
 * @Author: Wang Rui
 * @Date: $
 */
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
