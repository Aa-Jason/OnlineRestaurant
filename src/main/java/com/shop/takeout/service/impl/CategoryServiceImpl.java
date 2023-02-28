package com.shop.takeout.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.takeout.common.CustomException;
import com.shop.takeout.entity.Category;

import com.shop.takeout.entity.Dish;
import com.shop.takeout.entity.Setmeal;
import com.shop.takeout.mapper.CategoryMapper;
import com.shop.takeout.service.CategoryService;

import com.shop.takeout.service.DishService;
import com.shop.takeout.service.SetmealDishService;
import com.shop.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishQueryWrapper);
        if(count > 0){
            //已经关联菜品
            throw new CustomException("该分类已与菜品关联，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();
        if(count2 > 0) {
            //该分类与套餐有关联，抛出业务异常
            throw new CustomException("该分类已与套餐关联，不能删除");
        }
        //删除分类
        super.removeById(id);


    }
}
