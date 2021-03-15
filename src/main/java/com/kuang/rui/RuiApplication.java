package com.kuang.rui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.kuang.rui.mapper"})
public class RuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuiApplication.class, args);
    }

}
