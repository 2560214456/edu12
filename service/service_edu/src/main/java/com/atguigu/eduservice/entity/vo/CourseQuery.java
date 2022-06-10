package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "course查询对象",description = "课程查询对象封装")
@Data
public class CourseQuery  {
    @ApiModelProperty(value = "课程名称")
    private String title;
    @ApiModelProperty(value = "讲师id")
    private String teacherId;
    @ApiModelProperty(value = "一级分类id")
    private String OneSubjectId;
    @ApiModelProperty(value = "二级分类id")
    private String TwoSubject;
}
