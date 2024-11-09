package com.jonathan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jonathan.reggie.dto.DishDto;
import com.jonathan.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    // Add dish, and add the flavor data according to the dish,
    // two tables, dish and dish_flavor need to be operated.
    public void saveWithFlavor(DishDto dishDto);

    // Query dish and flavor based on id
    public DishDto getByIdWithFlavor(Long id);

    // Update dish and flavor info
    void updateWithFlavor(DishDto dishDto);
}
