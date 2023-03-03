package com.shop.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.takeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
