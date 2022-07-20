package com.lin.educenter.controller;

import com.google.gson.Gson;
import com.lin.JwtUtils;
import com.lin.educenter.pojo.UcenterMember;
import com.lin.educenter.service.UcenterMemberService;
import com.lin.educenter.utils.ConstantWxUtils;
import com.lin.educenter.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

//@CrossOrigin
//  不需要返回数据，用controller
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    UcenterMemberService ucenterMemberService;

//    生成微信扫描的二维码
//    不返回R
    @GetMapping("/login")
    public String getWxCode() throws UnsupportedEncodingException {
//        请求微信地址
//        再固定地址后面拼接参数
        String wxOpenAppId = ConstantWxUtils.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantWxUtils.WX_OPEN_APP_SECRET;
        String wxOpenRedirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
//        String url = "https://open.wexin.qq.com.connect/qrconnect?"+wxOpenAppId+"&response_type=code";

//       %s相当于?，表示占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";

//        需要对url进行编码，将特殊符合进行处理

        String redirectUrl = URLEncoder.encode(wxOpenRedirectUrl,"utf-8");
        String url = String.format(
                baseUrl,
                wxOpenAppId,
                redirectUrl,
                "youyu"
        );

//
        return "redirect:"+url;
    }

//    这种规则只是为了测试使用，在实际开发中，而不需要
//    http://localhost:8160/api/ucenter/wx/callback?code=021P2o000LHZ8O1Wr7300m9CmP3P2o09&state=youyu
//    这个路径必须是callback
//    获取扫描人信息，添加数据
//    扫完二维码后，默认调用callback方法
    @GetMapping("/callback")
    public String callback(String code,String state) throws Exception {
//        System.out.println(code+state);
//        1.获取code值（临时票据）
//        2.拿着code去请求微信的固定地址，拿到两个值，access_token(访问凭证)，openid
        String wxOpenAppId = ConstantWxUtils.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantWxUtils.WX_OPEN_APP_SECRET;
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
        "?appid=%s" +
        "&secret=%s" +
        "&code=%s" +
        "&grant_type=authorization_code";
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                wxOpenAppId,
                wxOpenAppSecret,
                code
        );
//        请求这个拼接好的地址，得到access_token(访问凭证)，openid
//        我们并不需要请求这个页面，所以要用到httpclient，得到返回结果
//        httpclient，不用浏览器，能模拟出浏览器的请求和响应
        String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
//        要从accessTokenInfo中获取access_token(访问凭证)，openid
//        因为accessTokenInfo是json格式，要获取其中变量和值，转为map格式
//        使用json转换工具Gson
        Gson gson = new Gson();
        HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
        String access_token = (String) map.get("access_token");
        String openid = (String) map.get("openid");

//        3.拿着得到的access_token，openid，再去访问微信提供的固定地址，获取到扫面人的信息
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
        "?access_token=%s" +
        "&openid=%s";
        String userInfoUrl = String.format(
                baseUserInfoUrl,
                access_token,
                openid
        );
        String userInfo = HttpClientUtils.get(userInfoUrl);
//        System.out.println(userInfo);
        HashMap map1 = gson.fromJson(userInfo, HashMap.class);
        String openid1 = (String) map1.get("openid");
        String nickname = (String) map1.get("nickname");
        String headimgurl = (String) map1.get("headimgurl");
//        Integer sex = (Integer) map1.get("sex");

//        扫码后相当于自动注册，所以加上注册
//        判断数据库是否有相同的微信信息
        UcenterMember member = ucenterMemberService.getOpenIdMember(openid1);
        if(member==null){
            member.setOpenid(openid1);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
//            member.setSex(sex);
            ucenterMemberService.save(member);
        }

        //        使用jwt根据member对象生成token字符串
//        通过路径传递token字符串
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return "redirect:http://localhost:3000?token="+token;
    }
}
