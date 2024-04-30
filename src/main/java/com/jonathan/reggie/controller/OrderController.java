package com.jonathan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jonathan.reggie.common.BaseContext;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.dto.OrdersDto;
import com.jonathan.reggie.entity.OrderDetail;
import com.jonathan.reggie.entity.Orders;
import com.jonathan.reggie.entity.User;
import com.jonathan.reggie.service.OrderDetailService;
import com.jonathan.reggie.service.OrderService;
import com.jonathan.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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


    /**
     * user order
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
     * 查看手机版历史纪录
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> pagePhone(int page,int pageSize){

        // 新创返回类型Page
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        // 用户ID
        Long currentId = BaseContext.getCurrentId();

        // 原条件写入
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,currentId);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        // 普通赋值
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        // 订单赋值
        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {

            // 新创内部元素
            OrdersDto ordersDto = new OrdersDto();

            // 普通值赋值
            BeanUtils.copyProperties(item,ordersDto);

            // 菜单详情赋值
            Long itemId = item.getId();

            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId,itemId);

            int count = orderDetailService.count(orderDetailLambdaQueryWrapper);

            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);

            ordersDto.setSumNum(count);

            ordersDto.setOrderDetails(orderDetailList);

            return ordersDto;
        }).collect(Collectors.toList());

        // 完成dishDtoPage的results的内容封装
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);
    }
}
