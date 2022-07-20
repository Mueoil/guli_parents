package com.lin.msmservice.controller;

import com.lin.R;
import com.lin.msmservice.service.MsmService;
import com.lin.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/edumsm/emailmsm")
//@CrossOrigin
public class EmailController {
    @Autowired
    MsmService msmService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @GetMapping("/sendEamilmsg/{email}")
    public R getEamilmsg(@PathVariable String email){
        //        1.先从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get("email");
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //        2.如果获取不到，进行邮箱发送
        code = RandomUtil.getFourBitRandom();
        boolean isSend = msmService.sendEmail(code,email);
        if(isSend){
            redisTemplate.opsForValue().set(email,code,3, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("发送验证码失败");
        }
    }
}
