package com.shop.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.takeout.entity.Setmeal;
import com.shop.takeout.mapper.SetmealDishMapper;
import com.shop.takeout.mapper.SetmealMapper;
import com.shop.takeout.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
