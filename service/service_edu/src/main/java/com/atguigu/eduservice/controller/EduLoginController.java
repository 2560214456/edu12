package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 登录
 */
@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin // 解决跨域
@Api(description = "用户登录实现")
public class EduLoginController {
    @ApiOperation(value = "登录用户")
    @PostMapping("/login")
    public Result login(){
        return Result.success().data("token","admin");
    }

    @ApiOperation(value = "返回用户基本信息")
    @GetMapping("/info")
    public Result info(){
        return Result.success().data("name","张三").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif").data("roles","admin");
    }
}
