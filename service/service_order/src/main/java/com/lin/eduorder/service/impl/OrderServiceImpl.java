package com.lin.eduorder.service.impl;

import com.lin.eduorder.client.EduClient;
import com.lin.eduorder.client.UcenterClient;
import com.lin.eduorder.pojo.Order;
import com.lin.eduorder.mapper.OrderMapper;
import com.lin.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.eduorder.utils.OrderNoUtil;
import com.lin.ordervo.CourseWebVoOrder;
import com.lin.ordervo.UcenterMemberOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-12
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

//    生成订单方法
//    需要课程信息和用户信息，通过远程调用实现
    @Autowired
    EduClient eduClient;
    @Autowired
    UcenterClient ucenterClient;
    @Override
    public String createOrders(String courseId, String uid) {
//        远程调用获取用户信息(根据用户id)
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(uid);
//        远程调用获取课程信息(根据课程id)
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(uid);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0); //0未支付 1已支付
        order.setPayType(1);        //1微信 2支付宝
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
