package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.dto.DishDto;
import com.jonathan.reggie.entity.Dish;
import com.jonathan.reggie.entity.DishFlavor;
import com.jonathan.reggie.mapper.DishMapper;
import com.jonathan.reggie.service.DishFlavorService;
import com.jonathan.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    /**
     * query dish and flavor based on id
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {
        //query dish basic info from dish
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //query dish flavor info from dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //update dish basic info
        this.updateById(dishDto);

        //clear current dish flavor -- delete from dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //add submitted dish flavor -- insert into dish_flavor
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
