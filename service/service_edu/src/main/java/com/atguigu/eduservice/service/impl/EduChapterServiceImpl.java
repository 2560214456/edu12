package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.CourseChapters.CourseChapters;
import com.atguigu.eduservice.entity.CourseChapters.CourseSection;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
   @Autowired
   EduVideoMapper videoMapper;
    //查询课程章节
    @Override
    public List<CourseChapters> getCourseChapterAndSections(String id) {
        //查询课程章节
        List<EduChapter> eduChapters = baseMapper.selectList(new LambdaQueryWrapper<EduChapter>()
                .eq(EduChapter::getCourseId, id));
        //查询课程小节
        List<EduVideo> eduVideos = videoMapper.selectList(new LambdaQueryWrapper<EduVideo>().eq(EduVideo::getCourseId, id));

        //封装为CourseChapters
        List<CourseChapters> list = new ArrayList<>();
        eduChapters.forEach(charter ->{
            CourseChapters courseChapters = new CourseChapters();
            BeanUtils.copyProperties(charter,courseChapters);
            eduVideos.forEach(video -> {
                if (video.getChapterId().equals(charter.getId())){
                    CourseSection courseSection = new CourseSection();
                    BeanUtils.copyProperties(video,courseSection);
                    courseChapters.addCourseSections(courseSection);
                }
            });
            list.add(courseChapters);
        });
        return list;
    }
    //删除章节
    @Override
    public boolean removeChapterById(String chapterId) {
        //查询该章节下是否有小节
        Integer integer = videoMapper.selectCount(new LambdaQueryWrapper<EduVideo>().eq(EduVideo::getChapterId, chapterId));
        if (integer > 0){
            throw new GuliException(20001,"请先删除该章节下的所有小节");
        }else{
            //没有小节删除该章节
            int i = baseMapper.deleteById(chapterId);
            return i > 0;
        }


    }
}
