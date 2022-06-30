package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.PayLog;
import com.atguigu.orderservice.mapper.PayLogMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.atguigu.orderservice.utlis.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-07
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {
    @Autowired
    OrderService orderService;
    //生成支付二维码
    @Override
    public Map createNatvie(String orderNo) {
        try {
            //1、根据订单号查询订单信息
            Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                    .eq(Order::getOrderNo, orderNo));
            //2、使用map设置生成二维码需要的参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954"); // 微信商户id
            m.put("mch_id", "1558950191"); // 商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串，然每个二维码都不一样
            m.put("body",order.getCourseTitle()); //课程名称（二维码要显示的名称）
            m.put("out_trade_no",orderNo); // 二维码唯一标识，订单号
            // 支付的金额
            m.put("total_fee",order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip","127.0.0.1");//支付的地址，（一般写项目的域名）
            //支付的回调，没有用到  http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("notify_url","http://guli.shop/api/order/weixinPay/weixinNotify\\n");
            m.put("trade_type","NATIVE");//支付的类型（比如：根据价格生成二维码）

            //3、发送httpclient请求，传递参数xml格式，微信支付提供的固定的地址
            //发送请求，请求https://api.mch.weixin.qq.com/pay/orderquery（固定地址）
            HttpClient httpClient =
                    new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置xml格式的参数 T6m9iK73b0kn9g5v426MKfHQH7X8rKwb 这个是微信开放平台商户key
            httpClient.setXmlParam(WXPayUtil
                    .generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.setHttps(true); // 支持https 访问
            //执行请求发送
            httpClient.post();
            //4、得到发送请求返回结果
            //返回的内容是xml格式
            String xml = httpClient.getContent();
            //把xml格式转换为map集合
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);
            // 最终返回数据 的封装
            Map map = new HashMap();
            map.put("out_trade_no",orderNo);// 订单id
            map.put("course_id",order.getCourseId()); //课程id
            map.put("total_fee",order.getTotalFee()); //课程价格
            map.put("result_code",resultMap.get("result_code"));//二维码操作状态码（不是支付是否成功状态码）
            map.put("code_url",resultMap.get("code_url")); // 二维码地址（URL地址）
            return map;
        }catch (Exception e){
            throw new GuliException(20001,"生成二维码失败");
        }
    }
    //根据订单号查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954"); // 微信商户id
            m.put("mch_id", "1558950191"); // 商户号
            m.put("out_trade_no", orderNo); //
            m.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串，然每个二维码都不一样
            //2、发送httpClient请求
            HttpClient httpClient =
                    new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //封装参数（xml格式）
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.setHttps(true); // 可以访问https协议
            httpClient.post(); // 发送请求
            //3、获取请求数据(获取的数据是xml格式)
            String xml = httpClient.getContent();
            //把xml转换为map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    //向支付表中添加记录，根据订单号修改订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order.getStatus().intValue() == 1)return;
        order.setStatus(1);
        orderService.updateById(order);
        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date()); // 支付完成时间
        payLog.setPayType(1);//支付类型 1 表示微信
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id")); // 流水号（每个都不一样）
        payLog.setAttr(JSONObject.toJSONString(map)); // 其他属性
        baseMapper.insert(payLog);//插入到支付日志表


    }
}
