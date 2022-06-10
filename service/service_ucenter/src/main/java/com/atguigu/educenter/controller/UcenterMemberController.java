package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.pojo.UserAndOrderVo;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
@Api(description = "用户登录And注册")
public class UcenterMemberController {
    @Autowired
    UcenterMemberService ucenterMemberService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody UcenterMember ucenterMember){
        String token = ucenterMemberService.login(ucenterMember);
        return Result.success().data("token",token).data("token",token);
    }
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return Result.success();
    }

    @ApiOperation(value = "根据token获取id查询用户数据查询")
    @GetMapping("/userInfo")
    public Result userInfo(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember user = ucenterMemberService.getById(userId);
        user.setPassword("");
        return Result.success().data("userInfo",user);
    }

    @ApiOperation(value = "根据用户id查询用户信息")
    @GetMapping("/getUserById/{userId}")
    public Result getUserById(@PathVariable String userId){
        UcenterMember user = ucenterMemberService.getById(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",user.getId());
        map.put("nickName",user.getNickname());
        map.put("avatar",user.getAvatar());
        return Result.success().data(map);
    }
    @ApiOperation(value = "根据用户id查询用户信息生成订单")
    @GetMapping("/getUserAddOrder/{userId}")
    public UserAndOrderVo getUserAddOrder(@PathVariable String userId){
        UserAndOrderVo vo = new UserAndOrderVo();
        UcenterMember user = ucenterMemberService.getById(userId);
        vo.setNickname(user.getNickname());
        vo.setMemberId(user.getId());
        vo.setMobile(user.getMobile());
        return vo;
    }

    @ApiOperation(value = "统计某一天注册的人数")
    @GetMapping("/countRegister/{sky}")
    public Result countRegister(@PathVariable String sky){
        Integer count = ucenterMemberService.getcountRegister(sky);
        return Result.success().data("countRegister",count);
    }

}

