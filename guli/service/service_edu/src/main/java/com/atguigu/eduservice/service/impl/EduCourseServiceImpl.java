package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.servicebase.config.exception.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-03-05
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    // 同时向课程表和简介表添加数据
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        // 这个baseMapper对应的是edu_course的Mapeer映射器
        int insert = baseMapper.insert(eduCourse);
        if (insert < 0) {
            throw new GuliException(20001, "添加课程失败");
        }

        // 为了对应关系，获取添加之后的课程id
        String cid = eduCourse.getId();
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        eduCourseDescription.setId(cid);
        eduCourseDescriptionService.save(eduCourseDescription);
        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        CourseInfoVo courseInfoVo = new CourseInfoVo();
        // 查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        // 查询描述表
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        // 返回值 1成功 0失败
        int res = baseMapper.updateById(eduCourse);
        if (res == 0) {
            throw new GuliException(20001, "添加课程失败");
        }

        // 为了对应关系，获取添加之后的课程id
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }
}
