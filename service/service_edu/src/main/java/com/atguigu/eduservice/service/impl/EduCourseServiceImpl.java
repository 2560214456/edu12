package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.*;
import com.atguigu.eduservice.entity.vo.*;
import com.atguigu.eduservice.mapper.EduCourseDescriptionMapper;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.*;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    EduCourseDescriptionMapper courseDescriptionMapper;  //课程简介
    @Autowired
    EduChapterService chapterService;  //章节
    @Autowired
    EduVideoService videoService; // 小节
    @Autowired
    VodClient vodClient; // 调用操作阿里云视频点播模块
    @Autowired
    EduCommentService commentService;
    //添加课程信息
    @Override
    public String saveCourseInfo(CourseInfoVO courseInfoVO) {
        //向课程表添加信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0){
            throw new GuliException(20001,"添加课程失败");
        }
        //向课程简介表添加信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVO,eduCourseDescription);
        eduCourseDescription.setId(eduCourse.getId());
        int insert1 = courseDescriptionMapper.insert(eduCourseDescription);
        if (insert1 == 0 ){
            throw new GuliException(20001,"添加课程描述失败");
        }
        return eduCourse.getId();
    }
    //根据id查询
    @Override
    public CourseInfoVO getCourseById(String id) {
        //创建CourseInfoVo对象
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        //查询课程基本信息
        EduCourse eduCourse = baseMapper.selectById(id);
        if (ObjectUtils.isNotNull(eduCourse)){
            BeanUtils.copyProperties(eduCourse,courseInfoVO);
        }else{
            throw new GuliException(20001,"课程基本信息为空");
        }
        //查询课程简介
        EduCourseDescription eduCourseDescription = courseDescriptionMapper.selectById(id);

        if (ObjectUtils.isNotNull(eduCourseDescription)){
            BeanUtils.copyProperties(eduCourseDescription,courseInfoVO);
        }else{
            throw new GuliException(20001,"课程描述信息为空");
        }
        return courseInfoVO;
    }
    //修改课程基本信息
    @Override
    public void updateCourse(CourseInfoVO courseInfoVO,String id) {
        //创建课程基本信息对和课程简介对象
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        //数据转换
        BeanUtils.copyProperties(courseInfoVO,eduCourse);
        BeanUtils.copyProperties(courseInfoVO,eduCourseDescription);
        eduCourseDescription.setId(eduCourse.getId());
        //修改数据
        baseMapper.updateById(eduCourse);
        courseDescriptionMapper.updateById(eduCourseDescription);
    }
    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo CoursePublishVo(String id) {
        CoursePublishVo coursePublishVo = baseMapper.getCoursePublishVo(id);
        return coursePublishVo;
    }
    //分页查询课程
    @Override
    public void pageQuery(Page<EduCourse> page1, CourseQuery courseQuery) {

        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EduCourse::getGmtCreate);
        wrapper.like(ObjectUtils.isNotNull(courseQuery.getTitle()),EduCourse::getTitle,courseQuery.getTitle());
        wrapper.eq(ObjectUtils.isNotNull(courseQuery.getTeacherId()),EduCourse::getTeacherId,courseQuery.getTeacherId());
        wrapper.eq(ObjectUtils.isNotNull(courseQuery.getOneSubjectId()),EduCourse::getSubjectParentId,courseQuery.getOneSubjectId());
        wrapper.eq(ObjectUtils.isNotNull(courseQuery.getTwoSubject()),EduCourse::getSubjectId,courseQuery.getTwoSubject());

        baseMapper.selectPage(page1,wrapper);
    }
    //删除 小节视频，课程小节，章节，描述，课程基本信息
    @Override
    public void deleteCourse(String courseId) {
        //查询小节视频id
        List<EduVideo> Videos = videoService.list(new LambdaQueryWrapper<EduVideo>().eq(EduVideo::getCourseId, courseId)
                .select(EduVideo::getVideoSourceId));
        List<String> videoIds = new ArrayList<>();
        Videos.forEach(video -> {
            if (ObjectUtils.isNotNull(video.getVideoSourceId()))
                videoIds.add(video.getVideoSourceId());
        });
        if (videoIds.size() > 0){
            Result result = vodClient.removeVideos(videoIds);
            if (result.getCode().equals(20001))
                throw new GuliException(20001,"删除视频失败");
        }
        //删除小节
        videoService.remove(new LambdaQueryWrapper<EduVideo>().eq(EduVideo::getCourseId,courseId));
        //删除章节
        chapterService.remove(new LambdaQueryWrapper<EduChapter>().eq(EduChapter::getCourseId,courseId));
        //删除课程简介
        courseDescriptionMapper.deleteById(courseId);
        //删除课程评论信息
        commentService.remove(new LambdaQueryWrapper<EduComment>()
        .eq(EduComment::getCourseId,courseId));
        //删除课程基本信息
        int i = baseMapper.deleteById(courseId);
        if (i < 1){
            throw new GuliException(20001,"删除失败");
        }
    }
    //分页条件查询（前台）
    @Override
    public void getCourseByQuery(Page<EduCourse> page, frontCourseQuery courseQuery) {
        boolean empty = StringUtils.isEmpty(null);
        baseMapper.selectPage(page,new LambdaQueryWrapper<EduCourse>()
        .eq(!StringUtils.isEmpty(courseQuery.getSubjectId()),EduCourse::getSubjectId,courseQuery.getSubjectId())
        .eq(!StringUtils.isEmpty(courseQuery.getSubjectParentId()),EduCourse::getSubjectParentId,courseQuery.getSubjectParentId())
        .orderByDesc(!StringUtils.isEmpty(courseQuery.getBuyCountSort()),EduCourse::getBuyCount)
        .orderByDesc(!StringUtils.isEmpty(courseQuery.getGmtCreateSort()),EduCourse::getGmtCreate)
        .orderByDesc(!StringUtils.isEmpty(courseQuery.getPriceSort()),EduCourse::getPrice)
        );
    }
    //前台根据课程id查询课程信息
    @Override
    public frontCourseVo getFrontCourseById(String courseId) {
        return baseMapper.getFrontCourseById(courseId);
    }
}
