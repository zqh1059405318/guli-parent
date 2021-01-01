package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-20
 */
@RestController
@RequestMapping("/eduservice/teacher")
@Api(tags = {"讲师管理"})
@CrossOrigin //解决跨域
public class EduTeacherController {
    // 注入service
    @Autowired
    private EduTeacherService eduTeacherService;

    //查询讲师所有数据
    @GetMapping("/findAll")
    @ApiOperation(value = "所有讲师列表")
    public R findAll() {
        List<EduTeacher> list = this.eduTeacherService.list(null);
        return R.ok().data("items", list);
    }

    // 根据ID对讲师进行删除
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        return flag ? R.ok() : R.error();
    }

    // 分页查询
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {

        // 创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        // 调用方法实现分页
        eduTeacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal(); // 总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); // list集合
        return R.ok().data("total", total).data("rows", records);
    }

    //条件查询带分页的方法
    //@RequestBody(required = false) 这个值可以没有的意思
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {

        Page<EduTeacher> page = new Page<>(current, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper();

        if (!StringUtils.isEmpty(teacherQuery.getName())) {
            wrapper.like("name", teacherQuery.getName());
        }

        if (!StringUtils.isEmpty(teacherQuery.getLevel())) {
            wrapper.eq("level", teacherQuery.getLevel());
        }

        if (!StringUtils.isEmpty(teacherQuery.getBegin())) {
            wrapper.ge("gmt_create", teacherQuery.getBegin());
        }

        if (!StringUtils.isEmpty(teacherQuery.getEnd())) {
            wrapper.le("gmt_modified", teacherQuery.getEnd());
        }

        wrapper.orderByDesc("gmt_modified");
        this.eduTeacherService.page(page, wrapper);
        long total = page.getTotal(); //总记录数
        List<EduTeacher> records = page.getRecords();  //list集合

        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "分页查询2")
    @GetMapping("pageTeacherCondition2/{current}/{limit}")
    public R pageTeacherCondition2(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
                    TeacherQuery teacherQuery) {

        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        // page或者pageQuery方法 运行之后，都是对方法的第一个参数，进行了筛选操作。
        eduTeacherService.pageQuery(pageTeacher, teacherQuery);

        List<EduTeacher> records = pageTeacher.getRecords();
        long total = pageTeacher.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    //添加讲师
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据讲师ID进行查询
    @GetMapping("getTeacher/{id}")
    @ApiOperation(value = "根据ID查询讲师")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {

        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }


    // 讲师修改
    // 在修改或者添加的时候，才会用@RequestBody注解，注解需要前端传回某个数据类型
    @PutMapping("updateTeacher/{id}")
    @ApiOperation(value = "根据ID修改讲师数据")
    public R updateById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher) {

        teacher.setId(id);
        boolean flag = eduTeacherService.updateById(teacher);
        return flag ? R.ok() : R.error();
    }

    //修改讲师
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = this.eduTeacherService.updateById(eduTeacher);
        return flag ? R.ok() : R.error();
    }

}

