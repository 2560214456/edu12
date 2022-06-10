package com.atguigu.eduservice.entity.vo;

import com.atguigu.eduservice.entity.EduCourse;
import lombok.Data;

//课程最终发布
@Data
public class CoursePublishVo extends EduCourse {
    //课程简介
    private String description;
    //课程一级分类
    private String OneSubject;
    //课程二级分类
    private String TwoSubject;
    //课程讲师
    private String teacherName;

}
