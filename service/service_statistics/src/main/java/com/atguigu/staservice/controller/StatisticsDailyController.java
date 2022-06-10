package com.atguigu.staservice.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.atguigu.commonutils.Result;
import com.atguigu.staservice.client.userClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-08
 */
@RestController
//@CrossOrigin
@Api(description = "数据统计")
@RequestMapping("/staservice/daily")
public class StatisticsDailyController {
    @Autowired
    StatisticsDailyService dailyService;
    @Autowired
    userClient userClient;

    @ApiOperation(value = "根据年份查询")
    @PostMapping("/addDaily/{day}")
    public Result getDailyByRears(@PathVariable String day){
        dailyService.createStatisticsByDay(day);
        return Result.success();
    }
    @ApiOperation(value = "查询统计数据")
    @GetMapping("/showChart/{begin}/{end}/{type}")
    public Result showChart(@PathVariable String begin,
                            @PathVariable String end,
                            @PathVariable String type){
        //begin:开始日期，end：结束日期，type：统计的数据（登录OR注册OR视频播放OR新增课程）
        Map<String,Object> map = dailyService.getChartData(begin,end,type);
        return Result.success().data(map);
    }

}

