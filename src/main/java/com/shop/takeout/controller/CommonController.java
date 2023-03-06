package com.shop.takeout.controller;

import com.shop.takeout.common.ResultUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@Api(tags = "文件上传与下载")
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${takeout.path}")
    private String basePath;


    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public ResultUtil<String> upload(MultipartFile file) {
        System.out.println(basePath);
        log.info(file.toString());
        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀，如 .jpg等
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //用UUID给图片生成一个新名字，防止上传的图片名称重复造成图片覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //判断目录是否存在，不存在则创建
        File dir = new File(basePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultUtil.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //response用于读入输入流
        try {
            //文件输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            //设置回复体格式
            response.setContentType("image/jpeg");

            //读取文件并写入response
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {//没读完就继续，-1是文件末尾
                outputStream.write(bytes,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
