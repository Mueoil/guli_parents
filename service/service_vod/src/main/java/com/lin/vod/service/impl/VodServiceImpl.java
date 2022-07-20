package com.lin.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.lin.exceptionHandler.GuliException;
import com.lin.vod.service.VodService;
import com.lin.vod.utils.ConstantVodUtils;
import com.lin.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideoAliyun(MultipartFile file) {
        try {
            //        accessKeyId,accessKeySecret
//        title 上传之后显示名称
//        fileName  上传文件原始名称
//        inputStream   上传文件的输入流
            String accessKeyId = ConstantVodUtils.Access_KEY_ID;
            String accessKeySercet = ConstantVodUtils.Access_KEY_SECRET;
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0,fileName.lastIndexOf("."));  //截取到.之前的文件名
            InputStream inputStream = null;
            inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,accessKeySercet,title,fileName,inputStream);
            UploadVideoImpl upload = new UploadVideoImpl();
            UploadStreamResponse response = upload.uploadStream(request);
            String videoId = "";
            if (response.isSuccess()) {     //不是判断是否上传成功，而是判断是否有回调值
                videoId = response.getVideoId();
            } else {
                /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                videoId = response.getVideoId();
            }
            System.out.println(videoId);
            return videoId;
        } catch (IOException e) {
            return  null;
        }

    }

    @Override
    public void removeMoreAliyunVideo(List videoIdList) {
        try {
            String access_key_id = ConstantVodUtils.Access_KEY_ID;
            String access_key_secret = ConstantVodUtils.Access_KEY_SECRET;
            DefaultAcsClient client = InitVodClient.initVodClient(access_key_id,access_key_secret);
            DeleteVideoRequest request = new DeleteVideoRequest();
//            videoIdList中的值变成1,2,3这种形式
//            因为request.setVideoIds只认识1,2,3这种字符串
            String list = StringUtils.join(videoIdList.toArray(),",");
            request.setVideoIds(list);
            client.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除多个视频失败");
        }

    }
}
