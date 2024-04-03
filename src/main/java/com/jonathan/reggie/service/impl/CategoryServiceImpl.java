package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.common.CustomException;
import com.jonathan.reggie.entity.Category;
import com.jonathan.reggie.entity.Dish;
import com.jonathan.reggie.entity.Setmeal;
import com.jonathan.reggie.mapper.CategoryMapper;
import com.jonathan.reggie.service.CategoryService;
import com.jonathan.reggie.service.DishService;
import com.jonathan.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * Delete categories based on ID. Judgment is required before deletion.
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //Add query conditions and query based on category ID
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //Query whether the current category is associated with a dish. If it is already associated,
        // throw a business exception.
        if(count1 > 0){
            //A dish has been associated and a business exception is thrown.
            throw new CustomException("There are dishes associated with the current category and cannot be deleted.");
        }

        //Check whether the current category is associated with a setmeal.
        // If it is already associated, throw a business exception.
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //Add query conditions and query based on category ID
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            //A setmeal has been associated and a business exception is thrown.
            throw new CustomException("There are setmeals associated with the current category and cannot be deleted.");
        }

        //Delete categories normally
        super.removeById(id);
    }
}
