package com.jonathan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    R<String> clean();
}
