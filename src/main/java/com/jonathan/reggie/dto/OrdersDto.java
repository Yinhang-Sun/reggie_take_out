package com.jonathan.reggie.dto;

import com.jonathan.reggie.entity.Orders;

import java.util.List;

public class OrdersDto extends Orders {
    private List<Orders> orders;
    private String userName;
}
