package com.jonathan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jonathan.reggie.dto.SetmealDto;
import com.jonathan.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    /**
     * save setmeal with dishes-associated
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
}
