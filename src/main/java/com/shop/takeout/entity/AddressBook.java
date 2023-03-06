package com.shop.takeout.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 地址簿
 */
@Data
@ApiModel("地址簿")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //用户id
    private Long userId;


    @ApiModelProperty("收货人")
    private String consignee;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;



    @ApiModelProperty("省级划分编号")
    private String provinceCode;


    //省级名称
    private String provinceName;


    @ApiModelProperty("市级划分编号")
    private String cityCode;


    //市级名称
    private String cityName;


    @ApiModelProperty("区级划分编号")
    private String districtCode;


    //区级名称
    private String districtName;


    //详细地址
    private String detail;


    //标签
    private String label;

    //是否默认 0 否 1是
    private Integer isDefault;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;
}
