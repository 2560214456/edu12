package com.atguigu.eduservice.client;

import com.atguigu.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodClientImpl.class) // 调用的服务模块名称
@Component
public interface VodClient {
    //定义调用的方法路径
    //根据视频id删除视频
    //@PathVariable 注解一定要指定参数名称，否则出错
    @DeleteMapping(value = "/eduvod/video/removeVideo/{videoId}")
    public Result removeVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping(value = "/eduvod/video/removeVideos")
    public Result removeVideos(@RequestParam("videoIds") List<String> videoIds);
}
