package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.Listener.SubjectExcelListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.Excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //课程分类列表（树形结构）
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有一级分类
        List<EduSubject> OneSubject = baseMapper.selectList(new LambdaQueryWrapper<EduSubject>().eq(EduSubject::getParentId, "0"));
        //查询所有二级分类
        List<EduSubject> TwoSubject = baseMapper.selectList(new LambdaQueryWrapper<EduSubject>().ne(EduSubject::getParentId, "0"));
        List<OneSubject> list = new ArrayList<>();
        //封装一级封路
        OneSubject.forEach(Subject -> {
            com.atguigu.eduservice.entity.subject.OneSubject oneSubject = new OneSubject();
            /*oneSubject.setId(Subject.getId());
            oneSubject.setTitle(Subject.getTitle());*/
            BeanUtils.copyProperties(Subject,oneSubject); // 把Subject 中的数据 set 到 oneSubject对象中
            TwoSubject.forEach(s ->{
                //封装二级分类
                com.atguigu.eduservice.entity.subject.TwoSubject twoSubject = new TwoSubject();
                if (oneSubject.getId().equals(s.getParentId())){
                    /*twoSubject.setId(s.getId());
                    twoSubject.setTitle(s.getTitle());*/
                    BeanUtils.copyProperties(s,twoSubject);// s 中的数据 set 到 twoSubject 对象中
                    oneSubject.addChildren(twoSubject);
                }

            });
            list.add(oneSubject);
        });
        return list;
    }
}
