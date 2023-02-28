package com.shop.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.takeout.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
