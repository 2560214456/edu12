package com.atguigu.cmsService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient // 注册服务
@ComponentScan(basePackages = {"com.atguigu"}) //扫描com.atguigu下的包
public class cmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(cmsApplication.class,args);
    }
}
