package com.shop.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
