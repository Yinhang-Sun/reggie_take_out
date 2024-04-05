package com.jonathan.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Address book
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //user id
    private Long userId;


    //consignee
    private String consignee;


    //phone number
    private String phone;


    //sex 0 F 1 M
    private String sex;


    //province code
    private String provinceCode;


    //province name
    private String provinceName;


    //city code
    private String cityCode;


    //city name
    private String cityName;


    //district code
    private String districtCode;


    //district name
    private String districtName;


    //detail
    private String detail;


    //label
    private String label;

    //isDefault 0 No 1 Yes
    private Integer isDefault;

    //create time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //update time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //create user
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //update user
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //deleted or not
    private Integer isDeleted;
}
