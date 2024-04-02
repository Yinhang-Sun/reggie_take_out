package com.jonathan.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Category
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //Type: 1-Food Category; 2-Combo Category
    private Integer type;


    //Category name
    private String name;


    //sort
    private Integer sort;


    //Created time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //Updated time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //Created user
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //update user
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //isDeleted
    private Integer isDeleted;

}
