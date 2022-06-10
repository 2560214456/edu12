package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.CourseChapters.CourseChapters;
import com.atguigu.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface EduChapterService extends IService<EduChapter> {
    //查询课程章节
    List<CourseChapters> getCourseChapterAndSections(String id);
    //删除章节
    boolean removeChapterById(String chapterId);
}
