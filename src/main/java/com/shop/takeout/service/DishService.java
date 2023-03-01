package com.shop.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.takeout.dto.DishDto;
import com.shop.takeout.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，并保存对应的口味
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
     DishDto getByIdWithFlavor(Long id);

    //修改菜品以及对应的口味信息
    void updateWithFlavor(DishDto dishDto);

    void deleteByIdsWithFlavor(Long[] ids);
}
