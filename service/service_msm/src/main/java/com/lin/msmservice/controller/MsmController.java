package com.lin.msmservice.controller;

import com.lin.R;
import com.lin.msmservice.service.MsmService;
import com.lin.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    MsmService msmService;

//    redis设置过期时间
    @Autowired
    RedisTemplate<String,String> redisTemplate;

//    发送短信的方法
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone){
//        1.先从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get("phone");
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
//        2.如果获取不到，进行阿里云发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> params = new HashMap<>();
        params.put("code",code);
        boolean isSend = msmService.send(params,phone);
        if(isSend){
//            发送成功后存到redis，同时设置有效时间
//            set存，get取
            System.out.println(phone);
            System.out.println(code);
            redisTemplate.opsForValue().set(phone,code,3, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }

    }
}
