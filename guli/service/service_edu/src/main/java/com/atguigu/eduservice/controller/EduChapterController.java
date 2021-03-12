package com.atguigu.eduservice.controller;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    //课程大纲列表
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable("courseId") String courseId) {
        List<ChapterVo> allChapterVideo = this.eduChapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo", allChapterVideo);
    }

    //添加章节
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        this.eduChapterService.save(eduChapter);
        return R.ok();
    }

    //根据章节id进行查询
    @GetMapping("/getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable("chapterId") String chapterId) {
        EduChapter eduChapter = this.eduChapterService.getById(chapterId);
        return R.ok().data("chapter", eduChapter);
    }

    //修改章节
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        this.eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除章节
    @DeleteMapping("/deleteChapter/{chapterId}")
    public R deleteChapter(@PathVariable String chapterId) {
        boolean flag = this.eduChapterService.deleteChapter(chapterId);
        return flag ? R.ok() : R.error();
    }

}
