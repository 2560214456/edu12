package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.Result;
import com.atguigu.staservice.client.userClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-08
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    userClient userClient;
    //添加统计数据（每天注册人数，视频播放次数，登录人数，新增视频，）
    @Override
    public void createStatisticsByDay(String day) {
        //删除同一天统计的数据
        baseMapper.delete(new LambdaQueryWrapper<StatisticsDaily>()
        .eq(StatisticsDaily::getDateCalculated,day));
        //添加统计数据
        // 根据日期查询注册的人数
        Result result = userClient.countRegister(day);
        Integer count = (Integer) result.getData().get("countRegister");
        StatisticsDaily daily = new StatisticsDaily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(count);
        daily.setLoginNum(RandomUtils.nextInt(100,200)); // TODO 登录人数
        daily.setVideoViewNum(RandomUtils.nextInt(100,200)); // TODO 每日视频播放次数
        daily.setCourseNum(RandomUtils.nextInt(100,200)); // TODO 每日新增课程数
        baseMapper.insert(daily);
    }
    //begin:开始日期，end：结束日期，type：统计的数据（登录OR注册OR视频播放OR新增课程）
    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.select("date_calculated",type);
        wrapper.between("date_calculated",begin,end);
        List<StatisticsDaily> dayList = baseMapper.selectList(wrapper);
        // 因为返回有两部分数据，日期 和 日期对应数量
        //前端要求数据json结构，对应后端java代码是list集合
        //创建两个list集合，一个日期list ，一个数量list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();
        //遍历查询所有集合数据list集合，进行封装
        dayList.forEach(day ->{
            date_calculatedList.add(day.getDateCalculated());
            switch (type){
                case "login_num":
                    numDataList.add(day.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(day.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(day.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(day.getCourseNum());
                    break;
                default:
                    break;
            }
        });
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
