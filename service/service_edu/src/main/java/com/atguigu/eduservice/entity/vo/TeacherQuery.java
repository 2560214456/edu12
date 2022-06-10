package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讲师 封装的查询条件
 */
@Data
public class TeacherQuery {
    @ApiModelProperty(value = "教师名称、模糊查询") // swagger 注解
    private String name;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;
    @ApiModelProperty(value = "查询开始时间",example = "2019-01-01 10:10:10")
    private String begin;
    @ApiModelProperty(value = "查询结束时间",example = "2020-01-01 10:10:10")
    private String end;
}
