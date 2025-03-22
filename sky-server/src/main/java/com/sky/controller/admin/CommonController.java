package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用的操作")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("上传的文件信息：{}", file);
        //获取文件的后缀
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        //UUID生成生成随机名称，防止覆盖了OSS中同名的文件
        String name = UUID.randomUUID() + extension;

        try {
            String filePath = aliOssUtil.upload(file.getBytes(), name);
            return Result.success(filePath);
        } catch (IOException e) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
