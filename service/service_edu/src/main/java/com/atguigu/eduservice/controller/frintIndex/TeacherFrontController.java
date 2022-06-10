package com.atguigu.eduservice.controller.frintIndex;

import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherFront")
//@CrossOrigin
@Api(description = "前台讲师页面")
public class TeacherFrontController {
    @Autowired
    EduTeacherService teacherService;
    @Autowired
    EduCourseService courseService;

    @ApiOperation(value = "分页查询讲师列表")
    @GetMapping("/teacherPage/{page}/{limit}")
    public Result teacherPage(@PathVariable long page,@PathVariable long limit){
        Page<EduTeacher> teacherPage = new Page<>(page,limit);
        teacherService.page(teacherPage, new LambdaQueryWrapper<EduTeacher>().orderByDesc(EduTeacher::getGmtCreate));
        Map<String,Object> map = new HashMap<>();
        map.put("records",teacherPage.getRecords());
        map.put("current",teacherPage.getCurrent());
        map.put("pages",teacherPage.getPages());
        map.put("hasPrevious",teacherPage.hasPrevious());
        map.put("hasNext",teacherPage.hasNext());
        map.put("total",teacherPage.getTotal());
        return Result.success().data("page",map);
    }
    @ApiOperation(value = "根据讲师id查询讲师信息和课程")
    @GetMapping("/teacher/{id}")
    public Result teacher(@PathVariable("id") String teacherId){
        EduTeacher teacher = teacherService.getById(teacherId);
        List<EduCourse> courses = courseService.list(new LambdaQueryWrapper<EduCourse>()
                .eq(EduCourse::getTeacherId, teacherId));
                //.last("limit 4")); //
        return Result.success() .data("teacher",teacher).data("courses",courses);
    }

}
