package com.atguigu.educenter.entity.vo;

import com.atguigu.educenter.entity.UcenterMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegisterVo extends UcenterMember {
    @ApiModelProperty(value = "验证码")
    private String code;
}
