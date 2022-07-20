package com.lin.educms.service;

import com.lin.educms.pojo.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-02
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //    查询所有的banner
    List<CrmBanner> selectAllBanner();
}
