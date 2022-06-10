package com.atguigu.cmsService.controller;


import com.atguigu.cmsService.entity.CrmBanner;
import com.atguigu.cmsService.service.CrmBannerService;
import com.atguigu.commonutils.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/cmsService/banneradmin")
//@CrossOrigin
@Api(description = "Banner轮播图,管理员使用")
public class BannerAdminController {
    @Autowired
    CrmBannerService bannerService;
    @ApiOperation(value = "分页查询")
    @GetMapping("/getBannerPage/{page}/{limit}")
    public Result grtAllBanner(    @PathVariable
                                   @ApiParam(value = "当前页") Integer page,
                                   @PathVariable
                                    @ApiParam(value = "每页显示总数") long limit){
        Page<CrmBanner> page1 = new Page<>(page,limit);
        bannerService.page(page1,null);
        return Result.success().data("bannerPage",page1);
    }
    @ApiOperation(value = "根据id获取Banner")
    @GetMapping("/getBannerById/{bannerId}")
    public Result getBannerById(@PathVariable @ApiParam(value = "BannerId") String bannerId){
        CrmBanner banner = bannerService.getById(bannerId);
        return Result.success().data("banner",banner);
    }
    @ApiOperation(value = "新增Banner")
    @PostMapping("/saverBanner")
    public Result saveBanner(@RequestBody CrmBanner banner){
        bannerService.save(banner);
        return Result.success();
    }
    @ApiOperation(value = "修改Banner")
    @PutMapping("/update")
    public Result updateById(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return Result.success();
    }
    @ApiOperation(value = "删除Banner")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable String id){
        bannerService.removeById(id);
        return Result.success();
    }
}

