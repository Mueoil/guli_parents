package com.lin.educms.controller;


import com.lin.R;
import com.lin.educms.pojo.CrmBanner;
import com.lin.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前台banner表 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-02
 */
@RestController
@RequestMapping("/educms/bannerfront")
//@CrossOrigin
public class BannerFrontController{
    @Autowired
    CrmBannerService crmBannerService;

//    查询所有的banner
    @GetMapping("/getAllBanner")
    public R getAllBanner(){
//        为了后面调用redis，这里就在实现层实现
        List<CrmBanner> list = crmBannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}

