package com.lin.eduorder.controller;


import com.lin.R;
import com.lin.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-12
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduorder/pay-log")
public class PayLogController {
    @Autowired
    PayLogService payLogService;

//    生成微信支付二维码
    @GetMapping("/createNative/{orderId}")
    public R createNative(@PathVariable String orderId){
//        返回信息包含二维码地址，还有其他信息
        Map map = payLogService.createNative(orderId);
        System.out.println("返回二维码的map集合："+map);
        return R.ok().data(map);
    }

//    根据订单号查询订单支付的状态
    @GetMapping("/queryPayStatus/{orderId}")
    public R queryPayStatus(@PathVariable String orderId){
        Map<String,String> map = payLogService.queryPayStatus(orderId);
        System.out.println("查询订单状态的map集合"+map);
        if(map == null){
            return R.error().message("支付出错了");
        }
//        如果不为空，可以获取状态
        if(map.get("trade_state").equals("SUCCESS")){
            //支付表加记录，更新订单表
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");

    }
}

