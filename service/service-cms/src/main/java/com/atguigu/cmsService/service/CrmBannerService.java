package com.atguigu.cmsService.service;

import com.atguigu.cmsService.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.boot.Banner;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> BannerList();
}
