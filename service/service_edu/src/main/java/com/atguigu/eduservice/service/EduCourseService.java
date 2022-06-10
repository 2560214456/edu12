package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface EduCourseService extends IService<EduCourse> {
    //添加课程信息
    String saveCourseInfo(CourseInfoVO courseInfoVO);
    //根据id查询
    CourseInfoVO getCourseById(String id);
    //修改课程基本信息
    void updateCourse(CourseInfoVO courseInfoVO,String id);
    //根据课程id查询课程确认信息
    CoursePublishVo CoursePublishVo(String id);
    //分页查询课程
    void pageQuery(Page<EduCourse> page1, CourseQuery courseQuery);
    //删除 课程小节，章节，描述，课程基本信息，课程评论信息
    void deleteCourse(String id);
    //分页条件查询（前台）
    void getCourseByQuery(Page<EduCourse> page, frontCourseQuery courseQuery);
    //前台 根据id查询课程信息
    frontCourseVo getFrontCourseById(String courseId);
}
