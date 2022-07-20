package com.lin.eduservice.client;

import org.springframework.stereotype.Component;

/**
 * 出错后执行此方法
 */
@Component
public class OrderFeignClien implements OrderClient{
    //    出错之后会执行
    @Override
    public Boolean isBuyCourse(String courseId, String memberId) {
        return null;
    }

}
