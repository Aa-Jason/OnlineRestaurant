package com.shop.takeout.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.takeout.entity.Employee;
import com.shop.takeout.mapper.EmployeeMapper;
import com.shop.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

}
