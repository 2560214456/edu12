package com.atguigu.staservice.timedTack;

import cn.hutool.core.date.DateUtil;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class tack {
    @Autowired
    StatisticsDailyService dailyService;

   /* @Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        System.out.println("每5秒执行");
    }*/
    /**
     * 每天凌晨1点执行定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task1(){
        // 获取上一天时间
        String say = DateUtil.formatDate(DateUtils.addDays(new Date(), -1));
        dailyService.createStatisticsByDay(say);
    }
}
