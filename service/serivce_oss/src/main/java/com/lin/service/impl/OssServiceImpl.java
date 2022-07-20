package com.lin.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lin.service.OssService;
import com.lin.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
//        工具类取值
        String endPoint = ConstantPropertiesUtils.END_POINT;
        String keyId = ConstantPropertiesUtils.KEY_ID;
        String keySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

//        创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);

//        上传文件流
        try {
            InputStream inputStream = file.getInputStream();
//            获取文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String filename = uuid + file.getOriginalFilename();

//            调用oss的方法实现上传
//            1.Bucket名称
//            2.上传到oss文件路径和文件名称 aa/bb/1.jpg     1.jpg（一般写文件名称）
//            3.上传文件的输入流


//            第二个参数可以按aa/bb/1.jpg
//            把文件按照日期进行分类       2019/11/12/1.jpg
            String date = new DateTime().toString("yyyy/MM/dd");
//            最终拼接
            filename = date + "/" + filename;
            ossClient.putObject(bucketName,filename,inputStream);

//            关闭oss
            ossClient.shutdown();

//            把上传之后的文件路径返回
//            需要把上传到阿里云oss路径手动拼接出来
//            https://edu-youyu.oss-cn-beijing.aliyuncs.com/a.jpg
            String url = "https://"+bucketName+"."+endPoint+"/"+filename;

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
