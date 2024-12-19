package com.qiu.api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(value = "com.qiu.api.mapper")
@EnableScheduling
@EnableDubbo
public class QiuapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QiuapiApplication.class, args);
    }

}
