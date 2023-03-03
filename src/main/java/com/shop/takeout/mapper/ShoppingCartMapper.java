package com.shop.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.takeout.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
