package com.jonathan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jonathan.reggie.dto.SetmealDto;
import com.jonathan.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * Save setmeal with dishes-associated
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * Delete combo/setmeal and at the same time dishes associated
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * Edit combo/setmeal and at the same time dishes associated
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
