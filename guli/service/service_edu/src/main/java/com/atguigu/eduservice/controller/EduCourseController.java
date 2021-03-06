package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    // service注入
    @Autowired
    EduCourseService eduCourseService;

    // 添加课程基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody(required = false) CourseInfoVo courseInfoVo) {
        String id =  eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

}

