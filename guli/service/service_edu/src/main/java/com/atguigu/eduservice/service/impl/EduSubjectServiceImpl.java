package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.listener.SubjectExcelListener;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-28
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        try {
            InputStream in = file.getInputStream();
            // 不能将listener直接交给spring管理，注解有Service
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 返回一个前端需要的树状结构的数据， 现在是数据库中已经有数据了
    @Override
    public List<OneSubject> showTreeData() {

        // 首先找到一级分类
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        // 匹配到parent_id为0，则说明是一级分类
        queryWrapper.eq("parent_id", 0);
        queryWrapper.orderByAsc("sort", "id");
        List<EduSubject> oneSubjects = baseMapper.selectList(queryWrapper);

        // 然后找到二级分类，也就是parent_id不为0的
        QueryWrapper<EduSubject> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.ne("parent_id", 0);
        queryWrapper2.orderByAsc("sort", "id");
        List<EduSubject> twoSubjects = baseMapper.selectList(queryWrapper2);

        //创建最终需要返回的OneSubject
        List<OneSubject> resList = new ArrayList<>();
        for (int i = 0; i < oneSubjects.size(); i++) {

            // 写入一级分类
            EduSubject eduSubject = oneSubjects.get(i);
            OneSubject oneSubject = new OneSubject();
            // oneSubject.setTitle(eduSubject.getTitle());
            // oneSubject.setId(eduSubject.getId());
            BeanUtils.copyProperties(eduSubject, oneSubject);
            resList.add(oneSubject);


            List<TwoSubject> twoList = new ArrayList<>();
            for (int j = 0; j < twoSubjects.size(); j++) {
                EduSubject twoEduSubject = twoSubjects.get(j);
                if(twoEduSubject.getParentId().equals(eduSubject.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoEduSubject, twoSubject);
                    twoList.add(twoSubject);
                }
            }

            oneSubject.setChildren(twoList);
        }
        return resList;
    }
}
