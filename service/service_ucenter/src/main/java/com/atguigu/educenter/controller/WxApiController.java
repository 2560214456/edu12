package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstanWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Api(description = "微信")
//@CrossOrigin
@Controller
@RequestMapping("/wxApi/open/wx")
public class WxApiController {
    @Autowired
    UcenterMemberService ucenterMemberService;
    //生成微信二维码
    @GetMapping("/login")
    @ApiOperation(value = "生成微信登录二维码")
    public String getWxCode(){
        //固定地址，在后面拼接参数
        /*String url = "： https://open.weixin.qq.com/connect/qrconnect?appid="+ ConstanWxUtils.WX_OPEN_APP_ID
                +"&response_type=code"; // 固定地址，在文档中有*/
        // 微信开放平台授权baseUrl / %s 占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url 进行URLEnacoder编码
        String redirect_url = ConstanWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirect_url = URLEncoder.encode(redirect_url,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //给 %s 占位符 赋值
        String url = String.format(
                baseUrl,
                ConstanWxUtils.WX_OPEN_APP_ID,
                redirect_url,
                "atguigu"

        );
        return "redirect:"+url;
    }
    //获取扫描人信息，添加数据
    @GetMapping("/callback")
    public String eallback(String code,String state){
        try {
            //获取code值，类似于验证码

            //拿着code请求 微信固定地址，得到两个值，access_token  和 openid
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接参数，id，秘钥 和 code 值
            String WxUrl = String.format(
                    baseAccessTokenUrl,
                    ConstanWxUtils.WX_OPEN_APP_ID,
                    ConstanWxUtils.WX_OPEN_APP_SECRET,
                    code);
            //请求这个拼接好的地址，得到返回的两个值，access_token 和 openid
            String accessToken =  HttpClientUtils.get(WxUrl);
            //从accessToken 获取access_token 和 openId
            //先把accessToken 转化为 map
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessToken, HashMap.class);
            String token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            //把用户数据添加到数据库
            //先判断数据库中有没有这个数据
            UcenterMember ucenterMember = ucenterMemberService.getOne(new LambdaQueryWrapper<UcenterMember>()
                    .eq(UcenterMember::getOpenid, openid));
            if (ObjectUtils.isEmpty(ucenterMember)){
                //为空 访问微信的资源服务器，获取用户信息
                //3、访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        token,
                        openid);
                //4、发送请求获取用户信息
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //5、获取返回返回userInfo字符串中扫码人信息
                HashMap userMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userMap.get("nickname");
                String headimgurl = (String) userMap.get("headimgurl"); //头像
                ucenterMember = new UcenterMember();
                ucenterMember.setOpenid(openid);
                ucenterMember.setNickname(nickname);
                ucenterMember.setAvatar(headimgurl);
                ucenterMemberService.save(ucenterMember);
            }
            //使用Jwt根据ucenterMember对象生成token字符串，
            //最后：返回首页，通过路径传递token字符串
            String Token1 = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
            return "redirect:http://localhost:3000?token="+Token1;
        } catch (Exception e) {
            throw  new GuliException(20001,"登录失败");
        }

    }
}
