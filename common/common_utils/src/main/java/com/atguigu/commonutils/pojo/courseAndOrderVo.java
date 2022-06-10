package com.atguigu.commonutils.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class courseAndOrderVo {
    @ApiModelProperty(value = "课程id")
    private String courseId;
    @ApiModelProperty(value = "课程名称")
    private String courseTitle;
    @ApiModelProperty(value = "课程封面")
    private String courseCover;
    @ApiModelProperty(value = "讲师名称")
    private String teacherName;
    @ApiModelProperty(value = "订单金额（分）")
    private BigDecimal totalFee;
}
