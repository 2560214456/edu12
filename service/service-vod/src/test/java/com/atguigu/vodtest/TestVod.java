package com.atguigu.vodtest;



import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;

import java.util.List;

public class TestVod {
    public static void main(String[] args) throws ClientException {
        //test1(); //根据视频id获取播放地址
        //test2(); //根据视频id获取播放视视频的凭证
       testUploadVideo("LTAI5tKgHQ7fcWaNsKGRffYw", "vVWWq3S9f34JCba4w5FAPFLPohflnS", "33", "D:/360downloads/01.mp4");
        //test4();  //根据视频id删除视频

    }

    //删除视频
    private static void test4(){
        //初始化
        DefaultAcsClient deleteClient = initObject.initVodClient("LTAI5tKgHQ7fcWaNsKGRffYw", "vVWWq3S9f34JCba4w5FAPFLPohflnS");
        DeleteVideoRequest request = new DeleteVideoRequest();
        DeleteVideoResponse response = new DeleteVideoResponse();
        // 可以传入多个id值，中间使用 , 逗号隔开
        request.setVideoIds("3868f306c32742e3958e821738484019,dc3f17f9b6874705855b4add4429b316");
        try{
            response = deleteClient.getAcsResponse(request);
        }catch (Exception e){
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
    /**
     * 视频上传
     * @param accessKeyId  id
     * @param accessKeySecret  秘钥
     * @param title 文件名称
     * @param fileName  文件路径
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        if (response.isSuccess()) {
            System.out.print("视频id=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("视频id=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
    // 获取播放地址
    public static   void test1() throws ClientException {
        //1、根据视频id获取视频的播放地址
        //创建初始化对象
        //创建获取视频地址request和response
        //向request对象里面设置视频id
        //调用初始化对象里面的方法，传递request，获取数据
        DefaultAcsClient defaultAcsClient = initObject.initVodClient("LTAI5tKgHQ7fcWaNsKGRffYw", "vVWWq3S9f34JCba4w5FAPFLPohflnS");
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("5802d41b99464d7eb5869cbe7eafa3e2");
        GetPlayInfoResponse response = defaultAcsClient.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("视频播放地址 = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("视频名称 = " + response.getVideoBase().getTitle() + "\n");
    }
    // 获取播放凭证
    public static void test2(){
        DefaultAcsClient defaultAcsClient = initObject.initVodClient("LTAI5tKgHQ7fcWaNsKGRffYw", "vVWWq3S9f34JCba4w5FAPFLPohflnS");
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse(); //response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest(); //request
        request.setVideoId("dc3f17f9b6874705855b4add4429b316");

        try {
            response = defaultAcsClient.getAcsResponse(request);
            //播放凭证
            System.out.print("播放凭证 = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
