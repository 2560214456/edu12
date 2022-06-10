package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-07
 */
public interface PayLogService extends IService<PayLog> {
    //生成支付二维码
    Map createNatvie(String orderNo);
    //根据订单号查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);
    //向支付表中添加记录，根据订单号修改订单状态
    void updateOrderStatus(Map<String, String> map);
}
