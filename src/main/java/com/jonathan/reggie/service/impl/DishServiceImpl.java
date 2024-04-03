package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.dto.DishDto;
import com.jonathan.reggie.entity.Dish;
import com.jonathan.reggie.entity.DishFlavor;
import com.jonathan.reggie.mapper.DishMapper;
import com.jonathan.reggie.service.DishFlavorService;
import com.jonathan.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * add dish, and save the dish flavor
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //save the dish basic info into dish table
        this.save(dishDto);

        Long dishId = dishDto.getId(); //dish id

        // dish flavors
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //save dish flavor into dish_flavor table
        dishFlavorService.saveBatch(flavors);

    }
}
