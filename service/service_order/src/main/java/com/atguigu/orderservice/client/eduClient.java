package com.atguigu.orderservice.client;

import com.atguigu.commonutils.pojo.courseAndOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-edu")
@Component
public interface eduClient {
    @GetMapping("/eduservice/courseFront/getCourseAddOrder/{courseId}")
    public courseAndOrderVo getCourseAddOrder(@PathVariable("courseId") String courseId);
}
