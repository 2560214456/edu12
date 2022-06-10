package com.atguigu.eduservice.controller.frintIndex;

import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/index")
@Api(description = "前台首页Controller")
public class indexController {
    @Autowired
    EduCourseService courseService;
    @Autowired
    EduTeacherService teacherService;
    // 查询课程 8 条 和 讲师 4 条 信息
    @GetMapping("/index")
    @ApiOperation(value = "查询课程8 和 讲师 4 条信息")
    public Result index(){
        List<EduCourse> courses = courseService.list(new LambdaQueryWrapper<EduCourse>()
                .orderByDesc(EduCourse::getGmtCreate)
                .eq(EduCourse::getStatus,"Normal")
                .last("limit 8"));
        List<EduTeacher> teachers = teacherService.list(new LambdaQueryWrapper<EduTeacher>()
                .orderByDesc(EduTeacher::getGmtCreate)
                .last("limit 4"));
        return Result.success().data("courses",courses).data("teachers",teachers);
    }
}
