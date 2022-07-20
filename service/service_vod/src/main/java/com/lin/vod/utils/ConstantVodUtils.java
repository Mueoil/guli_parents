package com.lin.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantVodUtils implements InitializingBean {
    @Value("${aliyun.vod.file.keyid}")
    private String keyid;
    @Value("${aliyun.vod.file.keysecret}")
    private String keysecret;


    public static String Access_KEY_ID;
    public static String Access_KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        Access_KEY_ID = keyid;
        Access_KEY_SECRET = keysecret;
    }
}
