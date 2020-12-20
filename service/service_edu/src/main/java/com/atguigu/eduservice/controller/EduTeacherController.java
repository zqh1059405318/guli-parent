package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-20
 */
@RestController
@CrossOrigin //跨域
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {
    // 注入service
    @Autowired
    private EduTeacherService teacherService;

    // rest风格接口
    @GetMapping("/findAll")
    public List<EduTeacher> findAll() {
        return teacherService.list(null);
    }

    @DeleteMapping("{id}")
    public boolean removeTeacher(@PathVariable String id) {
        return teacherService.removeById(id);
    }
}



