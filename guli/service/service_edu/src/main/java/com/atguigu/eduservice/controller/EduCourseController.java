package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

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

    //根据课程id查询课程确认信息
    @GetMapping("/getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo =  this.eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程最终发布 修改课程状态
    @PostMapping("/pulishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        this.eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    //返回课程列表
    @PostMapping("pageCourse/{current}/{limit}")
    public R pageTeacher(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody(required = false) CourseQuery courseQuery) {
        // 如果是分页查询的话，就要首先创建page对象
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        // 调用方法实现分页
        eduCourseService.pageQuery(pageCourse, courseQuery);
        long total = pageCourse.getTotal(); // 总记录数
        List<EduCourse> records = pageCourse.getRecords(); // list集合
        return R.ok().data("total", total).data("rows", records);
    }

    // 课程删除，要把课程本身/描述/章节/小节一起删除掉
    @DeleteMapping("deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable("courseId") String courseId){
        this.eduCourseService.removeCourse(courseId);
        return R.ok();
    }


}

