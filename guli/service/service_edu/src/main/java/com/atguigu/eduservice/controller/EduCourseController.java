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
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String id =  eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    // 根据课程id查询课程信息（再第二步要返回第一步的时候用得到）,返回的是前端数据的显示类，也就是courseInfoVo
    // PathVariable后面括号中 跟的是url中的值
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId") String courseId){
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo", courseInfoVo);
    }

    // 修改课程信息（返回第一步进行修改的时候需要调用）
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }
}

