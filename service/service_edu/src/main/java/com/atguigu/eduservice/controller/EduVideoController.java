package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/eduservice/video")
@Api(description = "章节中的小节")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    EduVideoService videoService;
    @Autowired
    VodClient vodClient; // 注入操作阿里云视频模块的接口

    @ApiOperation("/添加小节")
    @PostMapping("/addVideo")
    public Result addVodeo(@RequestBody EduVideo video  ){
        videoService.save(video);
        return Result.success();
    }
    @ApiOperation(value = "删除小节")
    @DeleteMapping("/removeVideo/{videoId}")
    public Result deleteVideo(@PathVariable String videoId){
        // 查询需要删除的小节视频id
        EduVideo one = videoService.getOne(new LambdaQueryWrapper<EduVideo>().eq(EduVideo::getId, videoId).select(EduVideo::getVideoSourceId));
        //根据视频id删除视频
        vodClient.removeVideo(one.getVideoSourceId());
        //删除小节
        videoService.removeById(videoId);
        return Result.success();
    }
    @ApiOperation(value = "根据id查询小节")
    @GetMapping("/getVideo/{videoId}")
    public Result getCideo(@PathVariable String videoId){
        EduVideo byId = videoService.getById(videoId);
        return Result.success().data("video",byId);
    }
    @ApiOperation(value = "修改小节")
    @PostMapping("/updateVideo")
    public Result updateVideo(@RequestBody EduVideo video){
        videoService.updateById(video);
        return Result.success();
    }

}

