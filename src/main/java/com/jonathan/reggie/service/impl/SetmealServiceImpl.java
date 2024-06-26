package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.common.CustomException;
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
     *
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


    /**
     * Delete combo/setmeal and at the same time dishes associated
     *
     * @param ids
     */
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1, 2, 3) and status = 1;
        // check the combo status, and determine if the combo can be deleted or not
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if (count > 0) {
            // if it can not be deleted, throw an exception
            throw new CustomException("The combo is on sale, and can not be deleted!");
        }

        // if it can be deleted, delete the data in the combo first
        this.removeByIds(ids);


        // delete from setmeal_dish where setmeal_id in (1, 2, 3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        // and delete the data in dishes associated
        setmealDishService.remove(lambdaQueryWrapper);
    }

    /**
     * Edit operation
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto){

        // First edit the information on the combo
        this.updateById(setmealDto);

        // edit internal dish operations ( delete first and then add)

        // delete
        Long setmealId = setmealDto.getId();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);

        setmealDishService.remove(queryWrapper);

        // add

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishService.saveBatch(setmealDishes);

    }


}
