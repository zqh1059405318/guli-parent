package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    // 返回一个前端需要的树状结构的数据
    @Override
    public List<HashMap<String, Object>> showTreeData() {
        List<HashMap<String, Object>> resData = new ArrayList<>();
        // 首先找到一级分类
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", 0);
        queryWrapper.orderByAsc("sort", "id");
        List<EduSubject> oneSubjects = baseMapper.selectList(queryWrapper);

        // 然后找到剩余的
        QueryWrapper<EduSubject> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.ne("parent_id", 0);
        queryWrapper2.orderByAsc("sort", "id");
        List<EduSubject> twoSubjects = baseMapper.selectList(queryWrapper);

        for (int i = 0; i < oneSubjects.size(); i++) {
            String parName = oneSubjects.get(i).getTitle();
            String oneID = oneSubjects.get(i).getId();
            List<String> children = new ArrayList<>();
            for (int j = 0; j < twoSubjects.size(); j++) {
                String parId = twoSubjects.get(j).getParentId();
                if (parId.equals(oneID)) {
                    children.add(twoSubjects.get(j).getTitle());
                }
            }

        }
        return null;
    }
}
