package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVO;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
@Api(description = "添加课程")
public class EduCourseController {
    @Autowired
    EduCourseService courseService;
    //添加课程基本信息
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("/addCourse")
    public Result addCourseInfo(@RequestBody CourseInfoVO courseInfoVO){
        String courseId = courseService.saveCourseInfo(courseInfoVO);
        return Result.success().data("id",courseId);
    }
    @ApiOperation(value = "修改课程基本信息")
    @PostMapping("/updateCourse/{id}")
    public Result updateCourse(@RequestBody CourseInfoVO courseInfoVO,@PathVariable String id){

        courseService.updateCourse(courseInfoVO,id);
        return Result.success();
    }
    @ApiOperation(value = "根据id查询课程基本信息")
    @GetMapping("/getCourseById/{id}")
    public Result getCourseById(@PathVariable String id){
        CourseInfoVO courseInfoVO = courseService.getCourseById(id);
        return Result.success().data("courseInfo",courseInfoVO);
    }
    @ApiOperation(value = "根据课程id查询课程确认信息")
    @GetMapping("/getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable String id){
        CoursePublishVo vo = courseService.CoursePublishVo(id);
        return Result.success().data("coursePublishVo",vo);
    }
    @ApiOperation(value = "课程最终发布")
    @PostMapping("/publishCourse/{id}")
    public Result publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal"); //设置课程发布状态 Draft未发布  Normal已发布
        courseService.updateById(eduCourse);
        return Result.success();
    }
    @ApiOperation(value = "分页OR查询课表列表")
    @PostMapping("/coursePage/{page}/{limit}")
    public Result coursePage(@ApiParam(name = "page",value = "当前页",required = true) @PathVariable Long page,
                             @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                             @ApiParam(name = "courseQuery",value = "查询对象",required = false) CourseQuery courseQuery){
        Page<EduCourse> page1 = new Page<>(page,limit);
        courseService.pageQuery(page1,courseQuery);
        return Result.success().data("page",page1);
    }
    @ApiOperation(value = "删除课程")
    @DeleteMapping("/deleteCourse/{id}")
    public Result deleteCourse(@PathVariable String id){
        //删除 课程小节，章节，描述，课程基本信息，课程评论信息
        courseService.deleteCourse(id);
        return Result.success();
    }

}

