package com.jonathan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.entity.Category;
import com.jonathan.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Category management
 */

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Adding new category
     *
     * @param category
     * @return
     */

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("category added successfully");
    }

    /**
     * pagination for category
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //Construct pagination constructor
        Page<Category> pageInfo = new Page<>(page, pageSize);

        //Construct conditional constructor
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //Add filter criteria, to sort according to sort in Category
        queryWrapper.orderByAsc(Category::getSort);

        //Execute query
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * delete category according to id
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("delete category, id is: {}", ids);

        //categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("category deleted successfully!");
    }

    /**
     * Modify category information based on id
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("Modify category information：{}",category);

        categoryService.updateById(category);

        return R.success("Modification of category information successful!");
    }

    /**
     * Query category data based on conditions
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //conditional constructor
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //Add conditions
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

        //Add sort conditions
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


}
