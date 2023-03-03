package com.shop.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.takeout.entity.Orders;


public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
