package com.atguigu.eduservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseEntity {
    @Autowired
    HttpServletRequest req;

    public Page getPage(long current,long limit){
        /*int current = ServletRequestUtils.getIntParameter(req, "current", 1);
        int limit = ServletRequestUtils.getIntParameter(req, "limit", 6);*/
        return new Page<>(current,limit);
    }
}
