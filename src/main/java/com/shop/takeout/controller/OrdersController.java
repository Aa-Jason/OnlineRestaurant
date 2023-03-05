package com.shop.takeout.controller;

import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.entity.Orders;
import com.shop.takeout.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/orders")
@RestController
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public ResultUtil<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);

        return ResultUtil.success("下单成功！");
    }
}
