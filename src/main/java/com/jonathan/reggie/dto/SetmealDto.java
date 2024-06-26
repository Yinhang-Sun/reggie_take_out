package com.jonathan.reggie.dto;

import com.jonathan.reggie.entity.Setmeal;
import com.jonathan.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
