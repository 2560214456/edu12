package com.atguigu.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utlis.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    //上传文件
    @Override
    public String upoadFileAvatar(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID; // id
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET; // 秘钥
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME; // Bucket名称
        String AvatarURL = null;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String filename = file.getOriginalFilename();
            //1、在文件名称里面添加唯一的值 replaceAll 把 - 替换为 ""
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid + filename;
            //1把文件按照日期进行分类
            // 2019/11/12/01.png
            // 获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename = datePath +"/"+ filename;
            // 调用oss方法实现上传
            //第一个参数：bucketName名称
            //第二个参数： 上传到oss文件路径和文件名称， /aa/bb/1.png
            //第三个参数： 上传文件的输入流
            ossClient.putObject(bucketName, filename, inputStream);
            // 把上传之后文件路径返回
            // https://edu-222000.oss-cn-guangzhou.aliyuncs.com/360wallpaper_dt.jpg
            AvatarURL = "https://"+bucketName+"."+endpoint+"/"+filename;
        } catch (OSSException oe) {
            System.out.println("请求已到达 OSS, "
                    + "但由于某种原因被错误响应拒绝.");
            System.out.println("错误信息:" + oe.getErrorMessage());
            System.out.println("错误代码:" + oe.getErrorCode());
            System.out.println("请求编号:" + oe.getRequestId());
            System.out.println("主机 ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("这意味着客户端在尝试与 OSS 通信时遇到了严重的内部问题 "
                    + "比如没有网络.");
            System.out.println("错误信息:" + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭OSSClient对象
            if (ossClient != null) {
                ossClient.shutdown();
            }
            // 返回上传的问价你的URL地址
            return AvatarURL;
        }
    }
}
