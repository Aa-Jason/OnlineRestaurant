package com.shop.takeout.controller;

import com.shop.takeout.service.OrderDetailService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "订单详情")
@Slf4j
@RequestMapping("/orderDetail")
@RestController
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;


}
