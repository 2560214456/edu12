package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.DeleteRoleRequest;
import com.aliyuncs.ram.model.v20150501.DeleteRoleResponse;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.Utils.ConstantVodUtils;
import com.atguigu.vod.Utils.InitObject;
import com.atguigu.vod.service.vodService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class vodServiceImpl implements vodService {
    @Value("${aliyun.oss.file.keyid}")
    private  String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private  String keysecret;
    //上传视频到阿里云
    @Override
    public String uploadVideoAly(MultipartFile file) {
        String videoId = null;
        try {
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0,fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            /***
             * 参数一： id
             * 参数二： 秘钥
             * 餐参数三： 在上传之后显示的名称
             * 参数四： 上传文件原始名称（就是文件名）
             * 参数五：上传文件流
             */
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET,title,fileName,inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            if (response.isSuccess()){
                videoId = response.getVideoId();
            }else{
                videoId = response.getVideoId();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return videoId;
    }
    //删除视频
    @Override
    public void removeVideoById(String videoId) {
        DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
        DeleteVideoRequest request = new DeleteVideoRequest();
        DeleteVideoResponse response = new DeleteVideoResponse();
        request.setVideoIds(videoId);
        try{
            response = client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除失败");
        }
    }
    //删除多个视频
    @Override
    public void removevideoIds(List<String> videoIds) {
        try{
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            DeleteVideoResponse response = new DeleteVideoResponse();
            //把videoIds 转换为 1,,2,3,
            String join = StringUtils.join(videoIds.toArray(), ",");
            request.setVideoIds(join);
            response = client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除失败");
        }
    }

    @Override
    public String getVideoPlayAuth(String videoId) {
        try {
            DefaultAcsClient client = InitObject
                    .initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            request.setVideoId(videoId);
            request.setAuthInfoTimeout(200L);//设置有效期，默认100秒
            response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return playAuth;
        } catch (ClientException e) {
            throw new GuliException(20001,"获取视频播放凭证失败");
        }
    }
}
