package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    //统计某一天注册的人数
    Integer getCountRegister(String sky);
}
