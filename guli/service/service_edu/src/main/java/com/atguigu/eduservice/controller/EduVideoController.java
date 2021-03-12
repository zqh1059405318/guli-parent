package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;
    //添加视频/小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        this.eduVideoService.save(eduVideo);
        return R.ok();
    }

    //修改小节
    @PostMapping("/updateVideo")
    public R updateChapter(@RequestBody EduVideo eduVideo) {
        this.eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    //删除小节,后面会同时删除视频
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteChapter(@PathVariable("videoId") String videoId) {
        boolean flag = this.eduVideoService.removeById(videoId);
        return flag ? R.ok() : R.error();
    }

    // 得到章节的具体信息，用于弹窗中数据的回显
    @GetMapping("/getVideoInfo/{id}")
    public R getVideoInfo(@PathVariable String id) {
        EduVideo eduVideo = this.eduVideoService.getById(id);
        return R.ok().data("video",eduVideo);
    }

}

