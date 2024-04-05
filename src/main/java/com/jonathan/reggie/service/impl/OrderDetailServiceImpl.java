package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.entity.OrderDetail;
import com.jonathan.reggie.mapper.OrderDetailMapper;
import com.jonathan.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
