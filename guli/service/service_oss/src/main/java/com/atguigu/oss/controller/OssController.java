package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileOss")
@CrossOrigin //解决跨域
public class OssController {
    //记得注入注解
    @Autowired
    private OssService ossService;

    // 上传
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public R uploadOssFile(@ApiParam(name = "file", value = "文件", required = true)
                           @RequestParam("file") MultipartFile file) {

        // 返回上传的文件路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
