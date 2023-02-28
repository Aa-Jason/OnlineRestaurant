package com.shop.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.entity.Employee;
import com.shop.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * 密码md5加密后进行对比
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public ResultUtil<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp == null){
            return ResultUtil.error("登录失败");
        }

        if(!emp.getPassword().equals(password)){
            return ResultUtil.error("密码不对，请重试！");
        }

        if(emp.getStatus() == 0){
            return ResultUtil.error("账号已禁用");
        }
        log.info("到了");
        //登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return ResultUtil.success(emp);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResultUtil<String> logout(HttpServletRequest request){
            //清理session中保存的id
            request.getSession().removeAttribute("employee");
            return ResultUtil.success("已成功退出！");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public ResultUtil<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(new Date());
//        employee.setUpdateTime(new Date());
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return ResultUtil.success("成功新增");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public ResultUtil<Page> page(int page, int pageSize, String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return ResultUtil.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public ResultUtil<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("账号信息："  + employee.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(new Date());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return ResultUtil.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public ResultUtil<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(id == null){
            return ResultUtil.error("查无此人");
        }
        return ResultUtil.success(employee);
    }
}
