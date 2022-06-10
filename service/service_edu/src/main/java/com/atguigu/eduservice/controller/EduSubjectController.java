package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
@Api(description = "课程分类信息管理")
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;
    //添加课程
    // 获取到上传的文件，把文件内容读取出来
    @PostMapping("/addSubject")
    @ApiOperation(value = "添加课程分类信息")
    public Result addSubject(MultipartFile file){
        //上传过来的文件
        subjectService.saveSubject(file,subjectService);
        return Result.success();
    }
    //课程分类列表功能（树形）
    @GetMapping("/getAllSubject")
    @ApiOperation(value = "课程分类列表功能（树形）")
    public Result getAllSubject(){
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return Result.success().data("list",list);
    }
    @ApiOperation(value = "查询所有一级分类")
    @GetMapping("/getOneSubject")
    public Result getOneSubject(){
        List<EduSubject> list = subjectService.list(new LambdaQueryWrapper<EduSubject>().eq(EduSubject::getParentId, "0"));
        return Result.success().data("oneSubject",list);
    }
    @ApiOperation(value = "根据一级分类id查询二级分类")
    @GetMapping("/getTwoSubject/{id}")
    public Result getTwoSubject(@PathVariable(value = "id") String OneSubjectId){
        List<EduSubject> list = subjectService.list(new LambdaQueryWrapper<EduSubject>().eq(EduSubject::getParentId, OneSubjectId));
        return Result.success().data("twoSubject",list);
    }
}

