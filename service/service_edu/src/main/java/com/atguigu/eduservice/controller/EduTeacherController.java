package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-25
 */
@Api(description = "讲师管理") // swagger 注解，controller说明
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin
public class EduTeacherController extends BaseEntity {
    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表") // swagger 注解 ，controller里面的方法说明
    @GetMapping("/findAll")
    public Result findAll(){
        List<EduTeacher> list = teacherService.list(null);
        return Result.success().data("items",list);
    }

    //@ApiParam(name = "id",value = "讲师ID") swagger 注解 ，controller中方法的参数描述
    // required 参数 是否是必须要有的 true 必须要有
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public Result removeTeacher(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id){
        boolean b = teacherService.removeById(id);
        if (b){
            return Result.success();
        }else {
            return Result.error();
        }
    }
    @GetMapping("/pageTeacher/{current}/{limit}")
    @ApiOperation(value = "分页查询")
    public Result pageListTeacher(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> page = new Page<>(current,limit);
        IPage page1 = teacherService.page(getPage(current,limit), null);
        IPage<EduTeacher> page2 = teacherService.page(page, null);
        return Result.success().data("TeacherPage",page1);
    }

    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    @ApiOperation(value = "分页条件查询")
    public Result pageTeacherCondition(@PathVariable Long current, @PathVariable Long limit, @RequestBody(required = false) TeacherQuery teacherQuery){
        LambdaQueryWrapper<EduTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(teacherQuery.getName()),EduTeacher::getName,teacherQuery.getName());
        wrapper.like(!StringUtils.isEmpty(teacherQuery.getLevel()),EduTeacher::getLevel,teacherQuery.getLevel());
        wrapper.ge(!StringUtils.isEmpty(teacherQuery.getBegin()),EduTeacher::getGmtCreate,teacherQuery.getBegin());
        wrapper.le(!StringUtils.isEmpty(teacherQuery.getEnd()),EduTeacher::getGmtCreate,teacherQuery.getEnd());
        wrapper.orderByDesc(EduTeacher::getGmtCreate);
        IPage page1 = teacherService.page(getPage(current,limit), wrapper);

        return Result.success().data("TeacherPage",page1);
    }

    @PostMapping("/addTeacher")
    @ApiOperation(value = "添加讲师")
    public Result addTeacher(@RequestBody EduTeacher teacher){
        boolean save = teacherService.save(teacher);
        if (save){
            return Result.success();
        }else{
            return Result.error();
        }
    }
    @GetMapping("/getTeacher/{id}")
    @ApiOperation(value = "根据id查询")
    public Result getTeacher(@PathVariable String id){
        EduTeacher byId = teacherService.getById(id);
        return Result.success().data("teacher",byId);
    }
    @PostMapping("/updateTeacher")
    @ApiOperation(value = "根据id修改")
    public Result updateTeacher(@RequestBody EduTeacher teacher){
        boolean b = teacherService.updateById(teacher);
        if (b){
            return Result.success();
        }else{
            return Result.error();
        }
    }
}

