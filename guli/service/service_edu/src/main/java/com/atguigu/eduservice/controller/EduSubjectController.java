package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-28
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;

    //添加课程分类，获取上传文件，然后读取文件内容，调用service方法，也可以同时传入service对象
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        // 不需要存储url，只是把上传的这个文件用easyexcel进行读取即可。
        eduSubjectService.saveSubject(file, eduSubjectService);
        return R.ok();
    }

    // 课程分类接口
    @GetMapping("getAllSubject")
    public R getAllSubject() {
        // List<HashMap<String, Object>> reslist = new ArrayList<>();
        List<HashMap<String, Object>> reslist = eduSubjectService.showTreeData();
        return R.ok();
    }
}

