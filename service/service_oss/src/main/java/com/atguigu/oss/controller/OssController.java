package com.atguigu.oss.controller;

import com.atguigu.commonutils.Result;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "上传头像")
@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    OssService ossService;
    //上传头像方法
    @PostMapping
    @ApiOperation("上传头像")
    public Result upoadOssFile(MultipartFile file){
        String AvatarURL = ossService.upoadFileAvatar(file);
        return Result.success().data("url",AvatarURL);
    }
}
