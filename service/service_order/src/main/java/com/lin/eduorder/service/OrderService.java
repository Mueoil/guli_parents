package com.lin.eduorder.service;

import com.lin.eduorder.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-12
 */
public interface OrderService extends IService<Order> {

    String createOrders(String courseId, String uid);
}
