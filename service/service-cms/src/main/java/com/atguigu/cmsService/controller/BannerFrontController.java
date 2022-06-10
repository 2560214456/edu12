package com.atguigu.cmsService.controller;


import com.atguigu.cmsService.entity.CrmBanner;
import com.atguigu.cmsService.service.CrmBannerService;
import com.atguigu.commonutils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/cmsService/bannerfront")
//@CrossOrigin
@Api(description = "Banner轮播图，前台查询")
public class BannerFrontController {
    @Autowired
    CrmBannerService bannerService;
    @ApiOperation(value = "获取首页banner")
    @GetMapping("/getAllBanner")
    public Result grtAllBanner(){

        List<CrmBanner> list = bannerService.BannerList();
        return Result.success().data("banners",list);
    }
}

