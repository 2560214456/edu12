package com.atguigu.eduservice.entity.vo;

import com.atguigu.eduservice.entity.EduCourse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "前台课程信息", description = "课程查询对象封装")
@Data
public class frontCourseVo extends EduCourse {

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;
    @ApiModelProperty(value = "讲师简介")
    private String teacherCareer;
    @ApiModelProperty(value = "讲师头像")
    private String teacherAvatar;
}
