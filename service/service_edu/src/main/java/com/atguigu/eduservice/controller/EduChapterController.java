package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.CourseChapters.CourseChapters;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器 课程章节
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/eduservice/chapter")
//@CrossOrigin
@Api(description = "课程章节")
public class EduChapterController {
    @Autowired
    EduChapterService chapterService;
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据课程id查询课程章节")
    public Result getById(@PathVariable String id){
        List<CourseChapters> courseChapters = chapterService.getCourseChapterAndSections(id);
        return Result.success().data("courseChapters",courseChapters);
    }
    @ApiOperation(value = "添加章节")
    @PostMapping("/addChapter")
    public Result addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return Result.success();
    }
    @ApiOperation(value = "根据id查询课程章节")
    @GetMapping("/getChapterInfo/{chapterId}")
    public Result getChapterInfo(@PathVariable String chapterId){
        EduChapter byId = chapterService.getById(chapterId);
        return Result.success().data("chapter",byId);
    }
    @ApiOperation(value = "修改章节")
    @PostMapping("/updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return Result.success();
    }
    @ApiOperation(value = "删除章节")
    @DeleteMapping(value = "/remove/{chapterId}")
    public Result removeChapter(@PathVariable String chapterId){
        boolean b = chapterService.removeChapterById(chapterId);
        if (b)
            return Result.success();
        else
            return Result.error();
    }
}

