package com.shop.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.config.BaseContext;
import com.shop.takeout.entity.AddressBook;
import com.shop.takeout.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "地址相关接口")
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @ApiOperation(value = "新增地址")
    @PostMapping
    public ResultUtil<AddressBook> save(@RequestBody AddressBook addressBook,HttpServletRequest request) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setCreateUser(userId);
        addressBook.setUpdateUser(userId);
        addressBookService.save(addressBook);
        return ResultUtil.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @ApiOperation(value = "设置默认地址")
    @PutMapping("default")
    public ResultUtil<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return ResultUtil.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @ApiOperation(value = "根据id查询地址")
    @GetMapping("/{id}")
    public ResultUtil get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return ResultUtil.success(addressBook);
        } else {
            return ResultUtil.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @ApiOperation(value = "查询默认地址")
    @GetMapping("default")
    public ResultUtil<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return ResultUtil.error("没有找到该对象");
        } else {
            return ResultUtil.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @ApiOperation(value = "查询指定用户所有地址")
    @GetMapping("/list")
    public ResultUtil<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return ResultUtil.success(addressBookService.list(queryWrapper));
    }

    /**
     * 修改用户地址信息
     * @param addressBook
     * @return
     */
    @ApiOperation(value = "修改用户地址信息")
    @PutMapping
    public ResultUtil<String> update(@RequestBody AddressBook addressBook) {
        log.info(addressBook.toString());
        addressBookService.updateById(addressBook);

        return ResultUtil.success("地址修改成功");
    }

    /**
     * 根据id删除用户地址
     * @param ids
     * @return
     */
    @ApiOperation(value = "根据id删除地址")
    @DeleteMapping
    public ResultUtil<String> delete(@RequestParam List<Long> ids) {
        log.info(ids.toString());
        //删除地址
        addressBookService.removeByIds(ids);
        return ResultUtil.success("删除地址成功");
    }
}
