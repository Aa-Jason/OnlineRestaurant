package com.shop.takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.entity.Category;
import com.shop.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResultUtil<String> save(@RequestBody Category category){
        categoryService.save(category);
        return ResultUtil.success("新增分类成功！");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public ResultUtil<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return ResultUtil.success(pageInfo);
    }

    /**
     * 根据id删除菜品
     * @param id
     * @return
     */
    @DeleteMapping
    public ResultUtil<String> delete(Long id){
        log.info("删除分类");
        categoryService.remove(id);
        return ResultUtil.success("分类删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public ResultUtil<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return ResultUtil.success("分类信息修改成功!");
    }

    //按条件查询
    @RequestMapping("/list")
    public ResultUtil<List<Category>> list( Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return ResultUtil.success(list);
    }
}
