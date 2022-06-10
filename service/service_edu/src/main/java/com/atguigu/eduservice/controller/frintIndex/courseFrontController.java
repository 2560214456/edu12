package com.atguigu.eduservice.controller.frintIndex;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.pojo.courseAndOrderVo;
import com.atguigu.eduservice.client.orderClient;
import com.atguigu.eduservice.client.ucenterUser;
import com.atguigu.eduservice.entity.CourseChapters.CourseChapters;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.vo.frontCourseQuery;
import com.atguigu.eduservice.entity.vo.frontCourseVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/courseFront")
@Api(description = "前台课程页面")
//@CrossOrigin
public class courseFrontController {
    @Autowired
    EduSubjectService subjectService;
    @Autowired
    EduCourseService courseService;
    @Autowired
    EduChapterService chapterService;
    @Autowired
    EduCommentService commentService;
    @Autowired
    ucenterUser ucenterUser;
    @Autowired
    orderClient orderClient;

    @ApiOperation(value = "查询所有课程类别")
    @GetMapping("/getSubject")
    public Result getSubject(){
        List<OneSubject> allOneTwoSubject = subjectService.getAllOneTwoSubject();
        return Result.success().data("subject",allOneTwoSubject);
    }
    @ApiOperation(value = "分页条件查询课程信息")
    @PostMapping("/getQueryCourse/{page}/{limit}")
    public Result getQueryCourse(@PathVariable long page,
                                @PathVariable long limit,
            @RequestBody frontCourseQuery frontCourseQuery){
        Page<EduCourse> coursePage = new Page<>(page,limit);
        courseService.getCourseByQuery(coursePage,frontCourseQuery);
        Map<String,Object> map = new HashMap<>();
        map.put("records",coursePage.getRecords());
        map.put("current",coursePage.getCurrent());
        map.put("pages",coursePage.getPages());
        map.put("hasPrevious",coursePage.hasPrevious());
        map.put("hasNext",coursePage.hasNext());
        map.put("total",coursePage.getTotal());
        return Result.success().data("coursePage",map);
    }

    @ApiOperation(value = "根据课程id查询课程详细信息")
    @PostMapping("/getCourseById/{courseId}")
    public Result getCourseById(HttpServletRequest request,
                                @PathVariable String courseId,@RequestHeader(value = "token",required = false)String token){
        Enumeration<String> headerNames = request.getHeaderNames();
        frontCourseVo frontCourseVo = courseService.getFrontCourseById(courseId);
        List<CourseChapters> chapters = chapterService.getCourseChapterAndSections(courseId);
        String userId = JwtUtils.getUserInfo(token);
        //根据课程id和用户id查询用户是否购买了该课程
        boolean buyCourse = false;
        if (!StringUtils.isEmpty(userId)){
             buyCourse = orderClient.isBuyCourse(courseId, userId);
        }

        return Result.success().data("course",frontCourseVo).data("chapter",chapters).data("byBuy",buyCourse);
    }
    @ApiOperation(value = "添加评论")
    @PostMapping("/addComment")
    public Result addComment(@RequestHeader(value = "token",required = false) String token,
                                       @RequestBody EduComment comment,HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        if (ObjectUtils.isEmpty(token))
            throw new GuliException(20001,"请先登录111");
        String userId = JwtUtils.getUserInfo(token);
        if (StringUtils.isEmpty(userId))
            throw new GuliException(20001,"请先登录");
        //查询课程信息
        EduCourse course = courseService.getById(comment.getCourseId());
        //查询用户id
        Result result = ucenterUser.getUserById(userId);
        Map<String, Object> data = result.getData();
        String userId1 = (String) data.get("userId");
        String nickName = (String) data.get("nickName");
        String avatar = (String) data.get("avatar");

        comment.setAvatar(avatar);
        comment.setNickname(nickName);
        comment.setMemberId(userId1);
        comment.setTeacherId(course.getTeacherId());
        commentService.save(comment);
        return Result.success();
    }
    @ApiOperation(value = "根据课程id查询课程评论信息")
    @GetMapping("/getCourseComment/{courseId}/{page}/{limit}")
    public Result getCourseComment(@ApiParam(value = "课程id") @PathVariable String courseId,
                                   @ApiParam(value = "当前页")@PathVariable long page,
                                   @ApiParam(value = "每页总数")@PathVariable long limit){
        Page<EduComment> commentPage = new Page<>(page,limit);
        commentService.page(commentPage,new LambdaQueryWrapper<EduComment>()
        .eq(EduComment::getCourseId,courseId)
        .orderByDesc(EduComment::getGmtCreate));
        return Result.success().data("commentPage",commentPage);
    }

    @ApiOperation(value = "根据课程id查询课程信息生成订单")
    @GetMapping("/getCourseAddOrder/{courseId}")
    public courseAndOrderVo getCourseAddOrder(@PathVariable String courseId){
        courseAndOrderVo vo = new courseAndOrderVo();
        frontCourseVo course = courseService.getFrontCourseById(courseId);
        vo.setCourseCover(course.getCover());
        vo.setCourseId(courseId);
        vo.setCourseTitle(course.getTitle());
        vo.setTeacherName(course.getTeacherName());
        vo.setTotalFee(course.getPrice());
        return vo;
    }
}
