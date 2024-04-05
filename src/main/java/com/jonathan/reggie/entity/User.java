package com.jonathan.reggie.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * user info
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //name
    private String name;


    //phone number
    private String phone;


    //sex 0 F 1 M
    private String sex;


    //id
    private String idNumber;


    //avatar
    private String avatar;


    //status 0:disable，1:enable
    private Integer status;
}
