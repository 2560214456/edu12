package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember user) {
        UcenterMember new_user = baseMapper.selectOne(new LambdaQueryWrapper<UcenterMember>()
                .eq(UcenterMember::getMobile, user.getMobile()));
        if (!ObjectUtils.isNotNull(new_user))
            throw new GuliException(20001,"手机号未注册，请先注册");
        if (!MD5.encrypt(user.getPassword()).equals(new_user.getPassword()))
            throw new GuliException(20001,"密码错误");
        if (new_user.getIsDeleted())
            throw new GuliException(20001,"账号已被禁用，请联系客服人员");
        // String jwtToken = JwtUtils.getJwtToken(new_user.getId(), new_user.getNickname());
        String jwtToken = JwtUtils.getJwtToken(new_user.getId(), new_user.getNickname());


        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        if (StringUtils.isEmpty(registerVo.getMobile()) || StringUtils.isEmpty(registerVo.getNickname())
            || StringUtils.isEmpty(registerVo.getPassword())|| StringUtils.isEmpty(registerVo.getCode())){
            throw new GuliException(20001,"注册失败");
        }
        String code = redisTemplate.opsForValue().get(registerVo.getMobile());
        if (!code.equals(registerVo.getCode()))
            throw new GuliException(20001,"验证码错误");

        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<UcenterMember>().eq(UcenterMember::getMobile, registerVo.getMobile()));

        if (count > 0)
            throw new GuliException(20001,"手机号已被注册");
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registerVo,ucenterMember);
        ucenterMember.setPassword(MD5.encrypt(ucenterMember.getPassword()));
        ucenterMember.setIsDeleted(false);
        ucenterMember.setAvatar("https://edu-222000.oss-cn-guangzhou.aliyuncs.com/2022/05/30/a29295dc2dfe4a53a1355387ec02911e360wallpaper.jpg");
        baseMapper.insert(ucenterMember);
    }
    //统计某一天注册的人数
    @Override
    public Integer getcountRegister(String sky) {
        return baseMapper.getCountRegister(sky);
    }
}
