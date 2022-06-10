package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface vodService {
    //上传视频到阿里云
    String uploadVideoAly(MultipartFile file);
    //删除视频
    void removeVideoById(String videoId);
    //删除多个视频
    void removevideoIds(List<String> videoIds);
    //获取视频凭证
    String getVideoPlayAuth(String videoId);
}
