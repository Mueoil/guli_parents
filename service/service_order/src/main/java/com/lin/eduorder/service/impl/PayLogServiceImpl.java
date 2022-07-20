package com.lin.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.lin.eduorder.pojo.Order;
import com.lin.eduorder.pojo.PayLog;
import com.lin.eduorder.mapper.PayLogMapper;
import com.lin.eduorder.service.OrderService;
import com.lin.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.eduorder.utils.ConstantWxOrderUtils;
import com.lin.eduorder.utils.HttpClient;
import com.lin.exceptionHandler.GuliException;
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
 * @author 有鱼
 * @since 2022-07-12
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {
    @Autowired
    OrderService orderService;


    @Override
    public Map createNative(String orderId) {
        try {
            String appId = ConstantWxOrderUtils.WX_OPEN_APP_ID;
            String partner = ConstantWxOrderUtils.WX_OPEN_APP_PARTNER;
            String partnerkey = ConstantWxOrderUtils.WX_OPEN_APP_PARTNERKEY;
            String notifyurl = ConstantWxOrderUtils.WX_OPEN_APP_NOTIFYURL;
//        1、根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderId);
            Order order = orderService.getOne(wrapper);

//        2、使用map设置二维码需要的参数
            Map map = new HashMap<>();
            map.put("appid", appId);
            map.put("mch_id", partner);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", order.getCourseTitle());
            map.put("out_trade_no", orderId);
            map.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");   //元角分转换成long
            map.put("spbill_create_ip", "127.0.0.1");   //baidu.com
            map.put("notify_url", notifyurl); //回调地址
            map.put("trade_type", "NATIVE");//支付类型

//        3、发送httpClient请求，传递参数xml格式，微信提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");  //这个地址是固定的
            client.setXmlParam(WXPayUtil.generateSignedXml(map,partnerkey));
            client.setHttps(true);//https不支持，要写上这个才支持
//            执行请求发送
            client.post();

//        4、得到发送请求返回结果
//            返回的内容是xml格式返回的，我们可以把xml格式转为map集合  这里不能用Gson
            String content = client.getContent();
            Map<String,String> resultMap = WXPayUtil.xmlToMap(content);

//            这里返回的resultMap只有二维码地址，而我们还需要其他信息，所以封装
            Map mapAdd = new HashMap<>();
            mapAdd.put("out_trade_no", orderId);
            mapAdd.put("course_id", order.getCourseId());
            mapAdd.put("total_fee", order.getTotalFee());
            mapAdd.put("result_code", resultMap.get("result_code"));    //返回二维码的状态码
            mapAdd.put("code_url", resultMap.get("code_url"));  //返回二维码的地址
            return mapAdd;
        }catch (Exception e){
            throw new GuliException(20001,"生成二维码失败");
        }
    }

//    查询订单的支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderId) {
        try {
            String appId = ConstantWxOrderUtils.WX_OPEN_APP_ID;
            String partner = ConstantWxOrderUtils.WX_OPEN_APP_PARTNER;
            String partnerkey = ConstantWxOrderUtils.WX_OPEN_APP_PARTNERKEY;
            String notifyurl = ConstantWxOrderUtils.WX_OPEN_APP_NOTIFYURL;
            Map map = new HashMap<>();
            map.put("appid", appId);
            map.put("mch_id", partner);
            map.put("out_trade_no", orderId);
            map.put("nonce_str", WXPayUtil.generateNonceStr());

//        2、发送httpclient请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");   //这个地址是固定的
            client.setXmlParam(WXPayUtil.generateSignedXml(map, partnerkey));
            client.setHttps(true);
            client.post();

            String content = client.getContent();
            Map<String,String> resultMap = WXPayUtil.xmlToMap(content);
            return resultMap;  //返回支付状态
        }catch (Exception e){
            throw new GuliException(20001,"获取支付状态失败");
        }
    }

    //支付表加记录，更新订单表
    @Override
    public void updateOrderStatus(Map<String, String> map) {
//        从查询的map获取订单号
        String orderNo = map.get("out_trade_no");
//        根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

//        更新订单表订单状态
        if(order.getStatus() == 1){
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);

//        向支付宝添加支付记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1); //1微信
        payLog.setTotalFee(order.getTotalFee());
        payLog.setTradeState(map.get("trade_state"));
        payLog.setTransactionId(map.get("transaction_id")); //订单流水号
        payLog.setAttr(JSONObject.toJSONString(map));       //其他内容传到表中的其他属性上
        baseMapper.insert(payLog);
    }
}
