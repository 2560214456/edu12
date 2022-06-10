package com.atguigu.msmsercice.controller;

import com.atguigu.commonutils.Result;
import com.atguigu.msmsercice.service.MsmService;
import com.atguigu.msmsercice.utlis.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api(description = "短信操作")
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @ApiOperation(value = "发送短信")
    @GetMapping("/send/{phone}")
    public Result sendMsm(@PathVariable String phone){
        //从redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(redisCode)){
            return Result.success().data("message","请稍后再试");
        }
        //生成随机数
        String code = RandomUtil.getFourBitRandom();
        Map<String,Object>param = new HashMap<>();
        param.put("code",code);
        //调用方法发送短信
        boolean isSend = msmService.send(param,phone);
        if (isSend){
            //把验证码存储在redis中  存储5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return Result.success();
        }else
            return Result.error(20001,"发送短信失败");
    }
}
