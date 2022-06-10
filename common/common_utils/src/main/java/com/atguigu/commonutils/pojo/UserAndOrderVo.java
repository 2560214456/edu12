package com.atguigu.commonutils.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAndOrderVo {
    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "会员昵称")
    private String nickname;

    @ApiModelProperty(value = "会员手机")
    private String mobile;
}
