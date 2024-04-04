package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.dto.SetmealDto;
import com.jonathan.reggie.entity.Setmeal;
import com.jonathan.reggie.entity.SetmealDish;
import com.jonathan.reggie.mapper.SetmealMapper;
import com.jonathan.reggie.service.SetmealDishService;
import com.jonathan.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * When adding a new set meal, you need to save the relationship between the set meal and the dishes.
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //Save the basic information of the setmeal, operate setmeal table and perform insert operation
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //Save the set meal and dishes, operate setmeal_dish table,
        //and perform insert operation
        setmealDishService.saveBatch(setmealDishes);
    }


}
