package com.jonathan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jonathan.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}