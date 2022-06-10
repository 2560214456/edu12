package com.atguigu.cmsService.service.impl;

import com.atguigu.cmsService.entity.CrmBanner;
import com.atguigu.cmsService.mapper.CrmBannerMapper;
import com.atguigu.cmsService.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.boot.Banner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {
    //最终在redis中生成的key的名称时 banner::selectBannerList
    @Cacheable(key = "'selectBannerList'",value = "banner")
    @Override
    public List<CrmBanner> BannerList() {
        List<CrmBanner> list = baseMapper.selectList(new LambdaQueryWrapper<CrmBanner>()
                .orderByDesc(CrmBanner::getGmtCreate)
                .last("limit 2"));
        return list;
    }
}
