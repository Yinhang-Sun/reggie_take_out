package com.jonathan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.dto.DishDto;
import com.jonathan.reggie.dto.SetmealDto;
import com.jonathan.reggie.entity.Category;
import com.jonathan.reggie.entity.Dish;
import com.jonathan.reggie.entity.Setmeal;
import com.jonathan.reggie.entity.SetmealDish;
import com.jonathan.reggie.service.CategoryService;
import com.jonathan.reggie.service.DishService;
import com.jonathan.reggie.service.SetmealDishService;
import com.jonathan.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * setmeal management
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    /**
     * Add setmeal/combo
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("Setmeal info：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("Added setmeal successfully!");
    }

    /**
     * combo/setmeal pagination
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //pagination constructor
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // Add query condition
        queryWrapper.like(name != null, Setmeal::getName, name);
        // add sorting condition
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //objects copy
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //object copy
            BeanUtils.copyProperties(item, setmealDto);
            //category id
            Long categoryId = item.getCategoryId();
            //query category object by id
            Category category = categoryService.getById(categoryId);
            //get category name
            if(category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * Delete combo/setmeal
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);

        setmealService.removeWithDish(ids);

        return R.success("Setmeal data deleted successfully!");
    }

    @PostMapping("/status/0")
    public R<String> closeStatus(@RequestParam List<Long> ids){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);

        List<Setmeal> setmeals = setmealService.list(queryWrapper);

        for (Setmeal setmeal:setmeals
        ) {
            setmeal.setStatus(0);
            setmealService.updateById(setmeal);
        }

        return R.success("Successfully modified！");
    }

    @PostMapping("/status/1")
    public R<String> openStatus(@RequestParam List<Long> ids){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);

        List<Setmeal> setmeals = setmealService.list(queryWrapper);

        for (Setmeal setmeal:setmeals
        ) {
            setmeal.setStatus(1);
            setmealService.updateById(setmeal);
        }

        return R.success("Successfully modified！");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){

        // We need to return setmealDto and define a new setmealDto to save data
        SetmealDto setmealDto = new SetmealDto();

        // Pass normal data in

        Setmeal setmeal = setmealService.getById(id);

        BeanUtils.copyProperties(setmeal,setmealDto);

        // Pass the dish information in

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);

        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);

        // return setmealDto
        return R.success(setmealDto);
    }


    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){

        setmealService.updateById(setmealDto);

        return R.success("Modified Successfully!");

    }

    /**
     * Query setmeal data based on conditions
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * Click on the setmeal picture to view the specific contents of the setmeal
     * The main information to be displayed on the front end is:
     * basic information of the dishes in the set meal, pictures, dish descriptions, and the number of dishes
     * @param SetmealId
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") Long SetmealId) {

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, SetmealId);

        // get data
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        List<DishDto> dishDtos = list.stream().map((setmealDish) -> {
            DishDto dishDto = new DishDto();

            // Copy of basic information
            BeanUtils.copyProperties(setmealDish, dishDto);

            // Set additional information
            Long dishId = setmealDish.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }

}