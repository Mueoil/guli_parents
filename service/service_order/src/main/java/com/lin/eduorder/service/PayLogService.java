package com.lin.eduorder.service;

import com.lin.eduorder.pojo.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-12
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderId);

    Map<String, String> queryPayStatus(String orderId);

    void updateOrderStatus(Map<String, String> map);
}
