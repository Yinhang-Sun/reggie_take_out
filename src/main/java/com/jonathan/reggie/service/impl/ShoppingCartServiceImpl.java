package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.entity.ShoppingCart;
import com.jonathan.reggie.mapper.ShoppingCartMapper;
import com.jonathan.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
