package com.atguigu.eduservice.entity.vo;

import com.atguigu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程类和 课程描述类的vo
 */
@Data
public class CourseInfoVO extends EduCourse {
    //课程基本信息
/*    @ApiModelProperty(value = "课程ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;
    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;
    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;*/

    //课程描述
    @ApiModelProperty(value = "课程简介")
    private String description;
}
