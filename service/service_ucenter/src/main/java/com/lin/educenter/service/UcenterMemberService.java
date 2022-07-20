package com.lin.educenter.service;

import com.lin.educenter.pojo.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.educenter.pojo.Vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-03
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid1);

    Integer countRegister(String day);
}
