package com.shop.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.takeout.dto.SetmealDto;
import com.shop.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增套餐，保存套餐与菜品的关联关系
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
