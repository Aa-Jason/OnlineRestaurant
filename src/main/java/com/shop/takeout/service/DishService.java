package com.shop.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.takeout.dto.DishDto;
import com.shop.takeout.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，并保存对应的口味
    public void saveWithFlavor(DishDto dishDto);

}
