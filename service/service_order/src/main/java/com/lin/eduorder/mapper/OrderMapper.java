package com.lin.eduorder.mapper;

import com.lin.eduorder.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-12
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
