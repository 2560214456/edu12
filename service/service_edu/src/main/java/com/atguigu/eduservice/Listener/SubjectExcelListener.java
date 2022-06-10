package com.atguigu.eduservice.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.Excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.annotation.Resource;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    EduSubjectService eduSubjectService;
    public SubjectExcelListener(){}
    public SubjectExcelListener(EduSubjectService eduSubjectService){
        this.eduSubjectService = eduSubjectService;
    }

    // 一行一行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null){
            throw  new GuliException(20001,"文件数据为空");
        }
        //一行一行读取，每次读取有两个值，第一个值一级分类（第二列内容），第二个值二级分类（第二列内容）
        EduSubject eduSubject1 = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if (eduSubject1 == null){
            // 没有相同的一级分类，进行添加
            eduSubject1 = new EduSubject();
            eduSubject1.setParentId("0");
            eduSubject1.setTitle(subjectData.getOneSubjectName()); // 一级分类名称
            eduSubjectService.save(eduSubject1);
        }
        //获取一级分类的id值
        String pid = eduSubject1.getId();
        EduSubject eduSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), pid);
        if (eduSubject == null){
            // 没有相同的二级分类，进行添加
            eduSubject1 = new EduSubject();
            eduSubject1.setParentId(pid);
            eduSubject1.setTitle(subjectData.getTwoSubjectName()); // 二级分类名称
            eduSubjectService.save(eduSubject1);
        }

    }
    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        EduSubject one = subjectService.getOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getTitle, name)
                .eq(EduSubject::getParentId, "0")
        );
        return one;
    }
    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String id){
        EduSubject Two = subjectService.getOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getTitle, name)
                .eq(EduSubject::getParentId, id)
        );
        return Two;
    }
    //完成之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
