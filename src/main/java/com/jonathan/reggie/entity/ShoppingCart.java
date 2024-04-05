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

    //name
    private String name;

    //user id
    private Long userId;

    //dish id
    private Long dishId;

    //setmeal id
    private Long setmealId;

    //dish flavor
    private String dishFlavor;

    //number
    private Integer number;

    //amount
    private BigDecimal amount;

    //image
    private String image;

    private LocalDateTime createTime;
}
