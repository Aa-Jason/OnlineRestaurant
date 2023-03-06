package com.shop.takeout.controller;

import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.entity.Orders;
import com.shop.takeout.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "点餐接口")
@Slf4j
@RequestMapping("/orders")
@RestController
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @ApiOperation(value = "下单")
    @PostMapping("/submit")
    public ResultUtil<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);

        return ResultUtil.success("下单成功！");
    }
}
