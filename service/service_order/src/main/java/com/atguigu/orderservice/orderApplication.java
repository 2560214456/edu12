package com.atguigu.orderservice;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient // 服务注册
@EnableFeignClients
@ComponentScan("com.atguigu")
@MapperScan("com.atguigu.orderservice.mapper")
public class orderApplication {
    public static void main(String[] args) {
        SpringApplication.run(orderApplication.class,args);
    }
}
