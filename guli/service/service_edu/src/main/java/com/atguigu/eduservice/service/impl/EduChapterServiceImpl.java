package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-03-05
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        // 根据章节id对应小节
        List<ChapterVo> chapterVos = new ArrayList<>();

        // 查询出courseId对应的所有章节
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_Id", courseId);
        List<EduChapter> chapters = baseMapper.selectList(queryWrapper);

        // 查询出courseId对应的所有小节
        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_Id", courseId);
        List<EduVideo> videos = eduVideoService.list(queryWrapper2);


        for (int i = 0; i < chapters.size(); i++) {

            ChapterVo chapterVo = new ChapterVo();
            EduChapter eduChapter = chapters.get(i);
            BeanUtils.copyProperties(eduChapter, chapterVo);
            String chapterId = chapterVo.getId();
            chapterVos.add(chapterVo);

            // 创建一个video队列
            List<VideoVo> children = new ArrayList<>();
            for (int j = 0; j < videos.size(); j++) {
                EduVideo eduVideo = videos.get(j);
                if (eduVideo.getChapterId().equals(chapterId)) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    children.add(videoVo);
                }
            }
            chapterVo.setChildren(children);
        }
        return chapterVos;
    }

    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_Id", chapterId);
        List<EduVideo> videos = eduVideoService.list(queryWrapper);
        if (videos.isEmpty()) {
            int res = baseMapper.deleteById(chapterId);
            return res > 0;
        } else {
            throw new GuliException(20001, "无法删除");
        }
    }

    @Override
    public void removeChapterCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
