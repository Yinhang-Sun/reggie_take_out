package com.jonathan.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jonathan.reggie.common.BaseContext;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.entity.ShoppingCart;
import com.jonathan.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * add shopping cart
     *
     * @param shoppingCart
     * @return
     */

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("Shopping cart: {}", shoppingCart);

        //set user id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //check if the dish / setmeal is in the cart or not
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            // adding dish to cart
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            // adding combo to cart
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        }

        //check if the dish / setmeal is in the cart or not
        //select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            //if exists, add the number by 1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //if not, add it to the cart and the default number is 1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     * check the shopping cart
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("checking the shopping cart...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * clean shopping cart
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        //delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("Cleared the shopping cart successfully!");

    }

    /**
     * ShoppingCart sub
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        // Set user
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        // Basic query condition
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        Long dishId = shoppingCart.getDishId();

        // Check it is dish or setmeal

        if(dishId != null){
            // Shopping cart is dish
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            // Shopping cart is setmeal
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        // Check the number. If it is a positive number, subtract one; if it is 0, throw an exception.
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        Integer number = shoppingCartServiceOne.getNumber();

        if (number != 0){
            shoppingCartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            shoppingCartService.removeById(shoppingCartServiceOne);
        }

        return R.success(shoppingCartServiceOne);
    }
}
