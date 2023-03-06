package com.shop.takeout.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.shop.takeout.common.ResultUtil;
import com.shop.takeout.config.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检测是否登录，防止没登录就访问页面
 */

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        //直接放行的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/swagger-resources",
                "/webjars/**",
                "/doc.html",
                "/v2/api-docs",
                "common/**"
        };
        boolean check = checkPath(urls, requestURI);
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        //获取后台管理端登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }

        //获取移动端登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            log.info("登录成功:{}",request.getSession().getAttribute("user").toString());
            return;
        }

        log.info("用户还未登录！");
        //没登录
        response.getWriter().write(JSONUtils.toJSONString(ResultUtil.error("NOTLOGIN")));
        return;

    }

    public boolean checkPath(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
