package com.jonathan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jonathan.reggie.common.BaseContext;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.dto.OrdersDto;
import com.jonathan.reggie.entity.OrderDetail;
import com.jonathan.reggie.entity.Orders;
import com.jonathan.reggie.entity.ShoppingCart;
import com.jonathan.reggie.entity.User;
import com.jonathan.reggie.service.OrderDetailService;
import com.jonathan.reggie.service.OrderService;
import com.jonathan.reggie.service.ShoppingCartService;
import com.jonathan.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * User order
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("Order data：{}",orders);
        orderService.submit(orders);
        return R.success("Ordered Successfully");
    }

    /**
     * Background echo
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> pagePC(int page, int pageSize, Long number, Date beginTime, Date endTime){

        // Customize basic page
        Page<Orders> pageInfo = new Page<>(page,pageSize);

        // Customize special Orders with names
        Page<OrdersDto> ordersDtoPage = new Page<>();

        // Writing constraints
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null,Orders::getId,number);
        if (beginTime != null && endTime != null){
            queryWrapper.between(Orders::getOrderTime,beginTime,endTime);
        }

        orderService.page(pageInfo, queryWrapper);

        // Ordinary assignment
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        // Order assignment
        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {

            // New internal elements
            OrdersDto ordersDto = new OrdersDto();

            // Ordinary assignment
            BeanUtils.copyProperties(item,ordersDto);

            // Special value assignment
            Long userId = item.getUserId();

            User user = userService.getById(userId);

            ordersDto.setUserName(user.getName());

            return ordersDto;
        }).collect(Collectors.toList());

        // Complete the content encapsulation of dishDtoPage’s results
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);
    }

    /**
     * Modify order status
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> beginSend(@RequestBody Orders orders){

        orderService.updateById(orders);

        return R.success("Modified Successfully");
    }

    /**
     * View mobile version history
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> pagePhone(int page,int pageSize){

        // New a return type page
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        // User id
        Long currentId = BaseContext.getCurrentId();

        // Original conditions
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,currentId);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        // Ordinary assignment
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        // Order assignment
        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {

            // New internal element
            OrdersDto ordersDto = new OrdersDto();

            // Ordinary assignment
            BeanUtils.copyProperties(item,ordersDto);

            // Dish details assignment
            Long itemId = item.getId();

            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId,itemId);

            int count = orderDetailService.count(orderDetailLambdaQueryWrapper);

            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);

            ordersDto.setSumNum(count);

            ordersDto.setOrderDetails(orderDetailList);

            return ordersDto;
        }).collect(Collectors.toList());

        // Complete the content encapsulation of dishDtoPage’s results
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);
    }

    //
    /**
     * We need to re-add the dishes in the order to the shopping cart,
     * so before that we need to clear the shopping cart (business layer implementation method)
     */
    @PostMapping("/again")
    public R<String> againSubmit(@RequestBody Map<String,String> map){
        // Get id
        String ids = map.get("id");

        long id = Long.parseLong(ids);

        // Create checking condition
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);

        //Get all order details corresponding to the order
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);

        //Clean the original shopping cart based on the user ID
        shoppingCartService.clean();

        //Get user id
        Long userId = BaseContext.getCurrentId();

        //  Assignment
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map((item) -> {

            // The following are all assignment operations

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setImage(item.getImage());

            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();

            if (dishId != null) {
                // If it is a dish, add the query conditions of the dish
                shoppingCart.setDishId(dishId);
            } else {
                // Added to cart is setmeal
                shoppingCart.setSetmealId(setmealId);
            }
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        // Insert shopping carts with data into the shopping cart table in batches
        shoppingCartService.saveBatch(shoppingCartList);

        return R.success("Operated successfully");
    }
}
