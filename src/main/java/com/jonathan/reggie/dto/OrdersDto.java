package com.jonathan.reggie.dto;

import com.jonathan.reggie.entity.OrderDetail;
import com.jonathan.reggie.entity.Orders;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrdersDto extends Orders {
    @Setter
    @Getter
    private List<Orders> orders;
    private String userName;
    @Setter
    private Integer sumNum;
    @Setter
    @Getter
    private List<OrderDetail> orderDetails;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
