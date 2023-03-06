package com.shop.takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.entity.User;
import com.shop.takeout.service.UserService;
import com.shop.takeout.utils.HanZiUtil;
import com.shop.takeout.utils.SMSUtils;
import com.shop.takeout.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "客户接口")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "发送验证码")
    @PostMapping("/sendMsg")
    public ResultUtil<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotBlank(phone)){
            //生成四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
//            SMSUtils.sendMessage("阿里云短信测试","测试专用模板",phone,code);
            session.setAttribute(phone,code);
            log.info("登录成功:{}",phone);
            //将验证码缓存5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return ResultUtil.success("短信验证码发送成功！");
        }
        return ResultUtil.error("短信发送失败");
    }

    /**
     * 移动端登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ResultUtil<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号与验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//        Object codeInSession = session.getAttribute(phone);

        Object codeInSession = redisTemplate.opsForValue().get(phone);


        if(codeInSession != null && codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){//说明是新用户，存入新用户的信息
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                user.setName(HanZiUtil.getRandomHanZi(3));
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            //登录成功则删除缓存的验证码
            redisTemplate.delete(phone);
            return ResultUtil.success(user);
        }
        return ResultUtil.error("登录失败！");

    }
}
