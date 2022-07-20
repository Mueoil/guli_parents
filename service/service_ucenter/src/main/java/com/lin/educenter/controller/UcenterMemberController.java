package com.lin.educenter.controller;


import com.lin.JwtUtils;
import com.lin.R;
import com.lin.educenter.pojo.UcenterMember;
import com.lin.educenter.pojo.Vo.RegisterVo;
import com.lin.educenter.service.UcenterMemberService;
import com.lin.ordervo.UcenterMemberOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-03
 */
@RestController
@RequestMapping("/educenter/ucenter-member")
//@CrossOrigin
public class UcenterMemberController {
    @Autowired
    UcenterMemberService ucenterMemberService;

//    登录
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
//        返回一个token值，使用jwt方式
        String token = ucenterMemberService.login(ucenterMember);
        return R.ok().data("token",token);
    }

//    注册
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

//    获取token用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
//        调用jwt工具类方法，根据request返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
//        根据id查询信息
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",ucenterMember);
    }

//    根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberOrder memberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,memberOrder);
        return memberOrder;
    }

//    查询某一天的注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = ucenterMemberService.countRegister(day);
        return R.ok().data("count",count);
    }
}

