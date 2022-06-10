package com.atguigu.eduservice.client;


import com.atguigu.commonutils.Result;
import org.springframework.stereotype.Component;

import java.util.List;
//点调用接口中的方法没有出错，这里类的方法不会执行，出错了就会执行
@Component
public class VodClientImpl implements VodClient {

    @Override
    public Result removeVideo(String videoId) {
        return Result.error("视频删除失败");
    }

    @Override
    public Result removeVideos(List<String> videoIds) {
        return Result.error("多个视频删除失败");
    }
}
