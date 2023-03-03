package com.shop.takeout.config;

import org.springframework.transaction.annotation.Transactional;

/**
 * 基于ThreadLocal封装工具类，用于设置和获取当前登录用户id
 */
@Transactional
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>() {
        long id = 0;
        // 解决办法就是初始化数值
        @Override
        protected Long initialValue() {
            return id;
        }
    };

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
