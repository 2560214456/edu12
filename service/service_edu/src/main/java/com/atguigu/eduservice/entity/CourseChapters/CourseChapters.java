package com.atguigu.eduservice.entity.CourseChapters;

import com.atguigu.eduservice.entity.EduChapter;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 返回前端的课程章节
 */
@Data
public class CourseChapters {
    private String id;
    private String title;
    //一个课程章节中有多个章节小节
    private List<CourseSection> courseSections = new ArrayList<>();

    public void addCourseSections(CourseSection courseSection){
        courseSections.add(courseSection);
    }

}
