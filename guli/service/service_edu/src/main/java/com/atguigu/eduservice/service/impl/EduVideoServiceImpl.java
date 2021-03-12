package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-03-05
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Override
    public void removeVideoCourseId(String courseId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }
}
