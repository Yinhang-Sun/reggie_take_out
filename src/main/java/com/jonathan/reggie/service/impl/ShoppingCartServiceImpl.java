package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.common.BaseContext;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.entity.ShoppingCart;
import com.jonathan.reggie.mapper.ShoppingCartMapper;
import com.jonathan.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    public R<String> clean(){

        // Compare users
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        // remove
        this.remove(queryWrapper);

        return R.success("clean successfully!");

    }
}
