package com.lin.eduorder.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantWxOrderUtils implements InitializingBean {

    @Value("${weixin.pay.appid}")
    private String appId;

    @Value("${weixin.pay.partner}")
    private String partner;

    @Value("${weixin.pay.partnerkey}")
    private String partnerKey;

    @Value("${weixin.pay.notifyurl}")
    private String url;


    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_PARTNER;
    public static String WX_OPEN_APP_PARTNERKEY;
    public static String WX_OPEN_APP_NOTIFYURL;
    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_PARTNER = partner;
        WX_OPEN_APP_PARTNERKEY = partnerKey;
        WX_OPEN_APP_NOTIFYURL = url;
    }
}
