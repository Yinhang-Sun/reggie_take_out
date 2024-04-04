package com.jonathan.reggie.controller;

import com.jonathan.reggie.common.R;
import com.jonathan.reggie.dto.SetmealDto;
import com.jonathan.reggie.service.CategoryService;
import com.jonathan.reggie.service.SetmealDishService;
import com.jonathan.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}