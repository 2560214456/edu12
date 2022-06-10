package com.atguigu.eduservice.client;

import com.atguigu.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface ucenterUser {
    //根据id查询用户信息
    @GetMapping(value = "/educenter/member/getUserById/{userId}")
    public Result getUserById(@PathVariable("userId") String userId);
}
