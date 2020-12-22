package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.config.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
// 只有带RestController才可以进行数据的传送与返回
@RestController
@CrossOrigin //跨域
@RequestMapping("/eduservice/teacher")
@Api(tags = {"讲师管理"})
public class EduTeacherController {
    // 注入service
    @Autowired
    private EduTeacherService teacherService;

    // rest风格接口

    // 查询所有讲师
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAll() {
        List<EduTeacher> list = teacherService.list(null);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            throw new GuliException(20001, "出现自定义的异常");
        }
        return R.ok().data("items", list);
    }

    // 根据ID对讲师进行删除
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
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
        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal(); // 总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); // list集合
        return R.ok().data("total", total).data("rows", records);
    }

    // 第一种方法，直接在controller中传入teacherQuery进行查询。
    // 条件查询
/*    @ApiOperation(value = "分页查询")
    @GetMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            TeacherQuery teacherQuery) {

        // 创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        // 构建QueryWrapper
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        // 判断条件值是否为空，不为空就进行拼接。
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if(null != level){
            wrapper.eq("level", level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified", end);
        }


        // 调用方法实现分页
        teacherService.page(pageTeacher, wrapper);

        long total = pageTeacher.getTotal(); // 总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); // list集合

        return R.ok().data("total", total).data("rows", records);

    }*/
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
        teacherService.pageQuery(pageTeacher, teacherQuery);

        List<EduTeacher> records = pageTeacher.getRecords();
        long total = pageTeacher.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    // 添加讲师的方法
    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        return save ? R.ok() : R.error();
    }

    //根据讲师ID进行查询
    @GetMapping("getTeacher/{id}")
    @ApiOperation(value = "根据ID查询讲师")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {

        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("item", teacher);
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
        boolean flag = teacherService.updateById(teacher);
        return flag ? R.ok() : R.error();
    }

}



