package com.lin.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.JwtUtils;
import com.lin.MD5;
import com.lin.educenter.pojo.UcenterMember;
import com.lin.educenter.mapper.UcenterMemberMapper;
import com.lin.educenter.pojo.Vo.RegisterVo;
import com.lin.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.exceptionHandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-03
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember ucenterMember) {
//        获取登录手机号码和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)) {
            throw new GuliException(20001,"登陆失败");
        }
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        if(member == null){
            throw new GuliException(20001,"用户不存在");
        }
        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(20001,"密码错误");
        }
        if(member.getIsDisabled()){ //为true表示被禁用
            throw new GuliException(20001,"用户被禁用");
        }
//        登陆成功，生成token
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
//        获取数据
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(nickname)||StringUtils.isEmpty(password)||StringUtils.isEmpty(code)){
            throw new GuliException(20001,"输入数据为空");
        }


//        判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码验证失败，注册失败");
        }

//        判断手机号是否相同
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"手机号已存在");
        }
        UcenterMember member = new UcenterMember();
        member.setMobile(registerVo.getMobile());
        member.setNickname(registerVo.getNickname());
        member.setPassword(MD5.encrypt(registerVo.getPassword()));
        member.setIsDisabled(false);
        member.setAvatar("beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(member);

    }

    @Override
    public UcenterMember getOpenIdMember(String openid1) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid1);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }
}
