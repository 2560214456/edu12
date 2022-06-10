package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.pojo.UserAndOrderVo;
import com.atguigu.commonutils.pojo.courseAndOrderVo;
import com.atguigu.orderservice.client.eduClient;
import com.atguigu.orderservice.client.userClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.utlis.OrderNoUtil;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-07
 */
@RestController
//@CrossOrigin
@Api(description = "订单")
@RequestMapping("/order/orderservice")
public class OrderController {
    @Autowired
    eduClient eduClient;
    @Autowired
    userClient userClient;
    @Autowired
    OrderService orderService;
    @ApiOperation(value = "生成订单")
    @GetMapping("/addOrder/{courseId}")
    public Result addOrder(@PathVariable String courseId, @RequestHeader(value = "token",required = false) String token){
        //获取用户id
        String UserId = JwtUtils.getUserInfo(token);
        if (StringUtils.isEmpty(UserId))
            throw new GuliException(20001,"请先登录");
        courseAndOrderVo vo = eduClient.getCourseAddOrder(courseId);
        UserAndOrderVo user = userClient.getUserAddOrder(UserId);
        Order order = new Order();
        BeanUtils.copyProperties(vo,order);
        BeanUtils.copyProperties(user,order);
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setPayType(1); //1表示微信支付 2支付宝（未实现）
        order.setStatus(0); //0表示未支付
        orderService.save(order);
        return Result.success().data("orderNo",order.getOrderNo());
    }
    @ApiOperation(value = "根据订单id查询订单信息")
    @GetMapping("/getOrderById/{orderId}")
    public Result getOrderById(@PathVariable String orderId){
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderId));
        return Result.success().data("order",order);
    }

}

