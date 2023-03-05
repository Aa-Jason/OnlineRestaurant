package com.shop.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.dto.DishDto;
import com.shop.takeout.entity.Category;
import com.shop.takeout.entity.Dish;
import com.shop.takeout.entity.DishFlavor;
import com.shop.takeout.service.CategoryService;
import com.shop.takeout.service.DishFlavorService;
import com.shop.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public ResultUtil<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        String key = "dish" + dishDto.getCategoryId() + "_1";//清理部分
        redisTemplate.delete(key);
        return ResultUtil.success("新增菜品成功");
    }

    /**
     * 分页查询菜品
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public ResultUtil<Page> page(int page, int pageSize, String name) {
        //设置分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //设置条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.eq(name != null, Dish::getName, name);
        dishService.page(pageInfo, lqw);

        //将dish分页查询除records的信息全部拷贝到dishDtoPage中
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //根据dish中的分类id查询数据给dishDto内的categoryName赋值
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //将item的值拷贝到dishDto内
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        //设置dishDto的records值
        dishDtoPage.setRecords(list);

        return ResultUtil.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/id")
    public ResultUtil<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return ResultUtil.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public ResultUtil<String> update(@RequestBody DishDto dishDto) {
//        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
//        Set keys = redisTemplate.keys("dish_*");//清理所有
        String key = "dish" + dishDto.getCategoryId() + "_1";//清理部分
        redisTemplate.delete(key);
        return ResultUtil.success("修改菜品信息成功！");
    }

    /**
     * 按条件查询对应菜品
     *
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public ResultUtil<List<Dish>> list(Dish dish){
//            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//            queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//            List<Dish> list = dishService.list(queryWrapper);
//            return ResultUtil.success(list);
//    }
    @GetMapping("/list")
    public ResultUtil<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;
        /**
         * 从redis中获取缓存数据，存在则返回，不存在则查询数据库，缓存到Redis
         */
        String key = "dish" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            return ResultUtil.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件 查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //缓存到Redis
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        return ResultUtil.success(dishDtoList);
    }
}
