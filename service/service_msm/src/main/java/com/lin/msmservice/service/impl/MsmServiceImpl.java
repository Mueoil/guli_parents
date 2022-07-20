package com.lin.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.lin.msmservice.service.MsmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Autowired
    JavaMailSenderImpl mailSender;
    @Override
    public boolean send(Map<String, Object> params, String phone) {
        if(StringUtils.isEmpty(phone)) return  false;
        DefaultProfile profile = DefaultProfile.getProfile("default","LTAI5tKP7BEWXrMjnyXkstqG","WNUDeEfg2NpOKbAfG2rabdcFRtc9P4");
        IAcsClient client = new DefaultAcsClient(profile);
        //        设置相关固定参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
//         设置发送相关的参数
        request.putQueryParameter("PhoneNumbers",phone); //1参数是固定的
        request.putQueryParameter("SignName","阿里云短信测试");    //申请阿里云，签名名称
        request.putQueryParameter("TemplateCode","SMS_154950909"); //申请阿里云，模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(params));    //把map转为JSON格式，因为收取验证码只能用json格式

//        最终的发送

        try {
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendEmail(String code, String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("【有鱼在线教育】 给您发送登录验证码，该验证码3分钟后失效！");
        simpleMailMessage.setText(code);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("1395234017@qq.com");
        if(!StringUtils.isEmpty(simpleMailMessage.getText())){
            mailSender.send(simpleMailMessage);
            return true;
        }else {
            return false;
        }
    }
}
