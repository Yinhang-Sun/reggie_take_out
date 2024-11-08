package com.jonathan.reggie.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Shopping cart
 */
@Data
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //Name
    private String name;

    //User id
    private Long userId;

    //Dish id
    private Long dishId;

    //Setmeal id
    private Long setmealId;

    //Dish flavor
    private String dishFlavor;

    //Number
    private Integer number;

    //Amount
    private BigDecimal amount;

    //Image
    private String image;

    private LocalDateTime createTime;
}
