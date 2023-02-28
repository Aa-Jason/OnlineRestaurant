package com.shop.takeout.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResultUtil<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.info("异常信息==>{}",exception.getMessage());

        //因为用户名已存在出现异常进入该分支
        if(exception.getMessage().contains("Duplicate entry")) {
            //异常样例信息为：  Duplicate entry 'ssj' for key
            String[] msgs = exception.getMessage().split(" ");
            return ResultUtil.error(msgs[2]+"已存在");
        }

        return ResultUtil.error("未知错误");
    }

    /**
     * 分类与菜品或套餐有关联时，不能删除，则抛出此异常
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultUtil<String> exceptionHandler(CustomException exception) {

        log.info("异常信息==>{}",exception.getMessage());
        return ResultUtil.error(exception.getMessage());
    }
}
