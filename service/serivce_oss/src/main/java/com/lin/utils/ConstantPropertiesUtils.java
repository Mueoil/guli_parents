package com.lin.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//因为使用了private，当项目启动，spring接口，spring加载之后，执行一个接口方法
//所以让此类实现一个接口
@Component
public class ConstantPropertiesUtils implements InitializingBean {
//    读取配置文件的内容

//    @Value("abc")
//    private String name;   //相当于 给name赋值

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;
    @Value("${aliyun.oss.file.bunketname}")
    private String bucketName;



//    实现了这个接口后，把前面都初始化之后，这个方法才会执行
//    定义一些公开静态常量
    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;

    }
}
