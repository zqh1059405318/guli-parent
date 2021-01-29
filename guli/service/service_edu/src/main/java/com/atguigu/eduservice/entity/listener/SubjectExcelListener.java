package com.atguigu.eduservice.entity.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.config.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    // easyExcel就是为了减少消耗，才一行一行读 所以才用了监听器
    // 一定是在监听器内部，才可以得到excel中的数据，如果我都读出来放在一个地方，那还不如不用，所以正确的应该是读一个就处理一个
    // 因此，应该在监听器内部，去进行读取数据的增删查改
    // 而监听器类无法交给spring进行管理，需要自己new对象，所以不能注入其他对象，也就无法使用servicemap实现数据库操作
    public EduSubjectService subjectService;
    public SubjectExcelListener() {
    }
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        // 文件为空，不需要读取
        if (subjectData == null) {
            throw new GuliException(20001, "文件数据为空");
        }

        // 添加一级分类，如果返回值不为空，说明有这个一级分类了，那么就不用重新添加了
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if(existOneSubject == null){ // 没有相同的一级分类
            // EduSubject是与mysql对应的表
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            // 存储到表里
            subjectService.save(existOneSubject);
        }

        String pid = existOneSubject.getId();
        //添加二级分类
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject == null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existTwoSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    //一级不可以重复添加 name为excel中的一级分类
    private EduSubject existOneSubject(EduSubjectService subjectService, String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        // 同时满足名字匹配的上，而且是一级。
        wrapper.eq("title", name);
        wrapper.eq("parent_id", 0);
        // getOne 返回的就是Service对应的entity对象
        return subjectService.getOne(wrapper);
    }

    //二级不可以重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService, String name, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        return subjectService.getOne(wrapper);
    }
}
