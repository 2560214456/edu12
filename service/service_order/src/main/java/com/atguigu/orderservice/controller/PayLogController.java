package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-07
 */
@RestController
@RequestMapping("/order/paylog")
//@CrossOrigin
@Api(description = "微信支付二维码")
public class PayLogController {
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "生成微信支付二维码")
    @GetMapping("/createNatvie/{orderNo}")
    public Result createNatvie(@PathVariable String orderNo){
        //返回的数据：包含二维码地址，还有其他信息
        Map map = payLogService.createNatvie(orderNo);
        System.out.println("-------------------------------------返回二维码Map集合"+map);
        return Result.success().data(map);
    }

    @ApiOperation(value = "查询订单支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo){
        Map<String,String>  map = payLogService.queryPayStatus(orderNo);
        System.out.println("****************************查询订单状态Map集合"+map);
        if(map == null){
            return Result.error("支付出错了");
        }
        //返回的map不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表，更新订单表状态
            payLogService.updateOrderStatus(map);
            return Result.success("支付成功");
        }
        return Result.success("支付中",25000);
    }

    @ApiOperation(value = "根据课程id和用户id查询课程是否购买")
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        int count = orderService.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, memberId)
                .eq(Order::getStatus, 1));

        return count>0;
    }
}

