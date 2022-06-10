package com.atguigu.orderservice.client;

import com.atguigu.commonutils.pojo.UserAndOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface userClient {
    @GetMapping("/educenter/member/getUserAddOrder/{userId}")
    public UserAndOrderVo getUserAddOrder(@PathVariable("userId") String userId);
}
