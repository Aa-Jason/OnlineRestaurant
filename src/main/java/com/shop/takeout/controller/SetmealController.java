package com.shop.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.dto.SetmealDto;
import com.shop.takeout.entity.Category;
import com.shop.takeout.entity.Setmeal;
import com.shop.takeout.service.CategoryService;
import com.shop.takeout.service.SetmealDishService;
import com.shop.takeout.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@Api(tags = "套餐管理")
@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Resource
    private CacheManager cacheManager;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public ResultUtil<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return ResultUtil.success("新增套餐成功！");
    }

    /**
     * 分页条件查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @ApiOperation(value = "分页条件查询")
    @GetMapping("/page")
    public ResultUtil<Page> page(int page, int pageSize, String name) {
        //设置分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //设置条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(pageInfo,lqw);

        //拷贝分页数据
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        //处理records数据的拷贝
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //套餐的分类id
            Long categoryId = item.getCategoryId();

            //根据id查询分类名称
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();

            //设置分类名称
            setmealDto.setCategoryName(categoryName);
            return setmealDto;

        }).collect(Collectors.toList());

        //设置records
        dtoPage.setRecords(list);

        return ResultUtil.success(dtoPage);
    }

    /**
     * 删除，批量删除套餐
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除套餐")
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public ResultUtil<String> delete(@RequestBody List<Long> ids){
        setmealService.removeWithDish(ids);
        return ResultUtil.success("删除成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public ResultUtil<List<Setmeal>> list( Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return ResultUtil.success(list);
    }

}
