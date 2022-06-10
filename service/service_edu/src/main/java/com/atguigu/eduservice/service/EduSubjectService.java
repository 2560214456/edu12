package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface EduSubjectService extends IService<EduSubject> {
    //把文件中的信息保存到数据库中 -- 添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService subjectService);
    //课程分类列表（树形结构）
    List<OneSubject> getAllOneTwoSubject();
}
