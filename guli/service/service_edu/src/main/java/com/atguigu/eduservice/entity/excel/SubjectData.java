package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

// 本类要和excel表中的进行对应
@Data
public class SubjectData {
    // 代表一级分类
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    // 代表二级分类
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
