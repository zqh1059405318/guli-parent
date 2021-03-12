package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
// 用于课程查询
public class CourseQuery {
    private String title;

    private String status;

    private String begin;

    private String end;
}
