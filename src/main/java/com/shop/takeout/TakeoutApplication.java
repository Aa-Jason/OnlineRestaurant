package com.shop.takeout;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@EnableKnife4j
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TakeoutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeoutApplication.class, args);
        log.info("起飞");
    }

}
