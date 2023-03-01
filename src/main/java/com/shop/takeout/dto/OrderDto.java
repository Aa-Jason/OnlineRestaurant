package com.shop.takeout.dto;


import com.shop.takeout.entity.OrderDetail;
import com.shop.takeout.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
