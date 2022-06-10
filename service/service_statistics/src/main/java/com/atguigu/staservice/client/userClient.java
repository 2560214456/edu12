package com.atguigu.staservice.client;

import com.atguigu.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface userClient {
    @GetMapping("/educenter/member/countRegister/{sky}")
    public Result countRegister(@PathVariable("sky") String sky);
}
