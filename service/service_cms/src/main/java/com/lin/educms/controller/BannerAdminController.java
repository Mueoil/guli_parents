package com.lin.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.R;
import com.lin.educms.pojo.CrmBanner;
import com.lin.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-02
 */
@RestController
@RequestMapping("/educms/bannerAdmin")
//@CrossOrigin
public class BannerAdminController {

    @Autowired
    CrmBannerService crmBannerService;
//    分页查询banner
    @GetMapping("pageBanner/{current}/{limit}")
    public R pageBanner(@PathVariable long current,@PathVariable long limit){
        Page<CrmBanner> pageBanner = new Page<>(current,limit);
        crmBannerService.page(pageBanner,null);
        List<CrmBanner> records = pageBanner.getRecords();
        long total = pageBanner.getTotal();
        return R.ok().data("items",records).data("total",total);
    }

//    根据Id查询Banner
    @GetMapping("/get/{id}")
    public R getById(@PathVariable String id){
        crmBannerService.getById(id);
        return R.ok();
    }

//    添加banner
    @PostMapping("/addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return R.ok();
    }

//    修改banner
    @PostMapping("/update")
    public R updateById(@RequestBody CrmBanner crmBanner){
        crmBannerService.updateById(crmBanner);
        return  R.ok();
    }

//    删除banner
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable String id){
        crmBannerService.removeById(id);
        return R.ok();
    }
}

