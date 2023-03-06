package com.shop.takeout.common;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回工具类
 * @param <T>
 */
@Data
@ApiModel("返回结果")
public class ResultUtil<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> ResultUtil<T> success(T object) {
        ResultUtil<T> r = new ResultUtil<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> ResultUtil<T> error(String msg) {
        ResultUtil r = new ResultUtil();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public ResultUtil<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
