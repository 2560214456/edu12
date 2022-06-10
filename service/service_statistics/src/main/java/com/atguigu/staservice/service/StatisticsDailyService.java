package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-08
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {
    //添加统计数据（每天注册人数，视频播放次数，登录人数，新增视频，）
    void createStatisticsByDay(String day);
    //begin:开始日期，end：结束日期，type：统计的数据（登录OR注册OR视频播放OR新增课程）
    Map<String, Object> getChartData(String begin, String end, String type);
}
