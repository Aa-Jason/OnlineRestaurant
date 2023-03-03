package com.shop.takeout.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入操作，自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime",new Date());
        metaObject.setValue("updateTime",new Date());
        log.info("id:{}",BaseContext.getCurrentId());
//        metaObject.setValue("createUser", BaseContext.getCurrentId());
//        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        this.strictInsertFill(metaObject,"updateUser",Long.class,BaseContext.getCurrentId());
        this.strictInsertFill(metaObject,"createUser",Long.class,BaseContext.getCurrentId());
    }

    /**
     * 更新操作，自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
//        metaObject.setValue("updateTime", new Date());
//        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.info("id:{}",BaseContext.getCurrentId());
        this.strictInsertFill(metaObject,"updateUser",Long.class,BaseContext.getCurrentId());
        this.strictInsertFill(metaObject,"updateTime",Date.class,new Date());
        this.strictInsertFill(metaObject,"updateUser",Long.class,BaseContext.getCurrentId());

    }
}
