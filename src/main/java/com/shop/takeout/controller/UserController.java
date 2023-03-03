package com.shop.takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.entity.User;
import com.shop.takeout.service.UserService;
import com.shop.takeout.utils.HanZiUtil;
import com.shop.takeout.utils.SMSUtils;
import com.shop.takeout.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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

        Object codeInSession = session.getAttribute(phone);
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
            return ResultUtil.success(user);
        }
        return ResultUtil.error("登录失败！");

    }
}
