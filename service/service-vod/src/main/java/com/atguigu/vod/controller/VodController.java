package com.atguigu.vod.controller;

import com.atguigu.commonutils.Result;
import com.atguigu.vod.service.vodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@Api(description = "阿里云视频点播")
//@CrossOrigin
public class VodController {
    @Autowired
    private vodService vodService;
    @ApiOperation(value = "上传视频到阿里云")
    @PostMapping("/uploadAlyVideo")
    public Result uploadAlyVideo(MultipartFile file){
        String VideoId = vodService.uploadVideoAly(file);
        return Result.success().data("videoId",VideoId);
    }
    @ApiOperation(value = "根据视频id删除视频")
    @DeleteMapping("/removeVideo/{videoId}")
    public Result removeVideo(@PathVariable String videoId){
        vodService.removeVideoById(videoId);
        return Result.success();
    }
    @ApiOperation(value = "删除多个视频")
    @DeleteMapping("/removeVideos")
    public Result removeVideos(@RequestParam("videoIds")List<String> videoIds){
        vodService.removevideoIds(videoIds);
        return Result.success();
    }
    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("/getVideoPlayAuthById/{videoId}")
    public Result getVideoPlayAuthById(@PathVariable String videoId){
       String videoPlayAuth = vodService.getVideoPlayAuth(videoId);
       return Result.success().data("playAuth",videoPlayAuth);
    }
}
