package com.atguigu.eduservice.entity.vo;

import lombok.Data;

// 用于发布时候的展示Vo
@Data
public class CoursePublishVo {
    private String id;

    private String title;

    private String cover;

    private Integer lessonNum;

    private String subjectLevelOne;

    private String subjectLevelTwo;

    private String teacherName;

    private String price;
}
