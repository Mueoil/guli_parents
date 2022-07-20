package com.lin.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadVideoAliyun(MultipartFile file);


    //    删除OSS多个视频的方法，所以传递多个视频id，List存放
    void removeMoreAliyunVideo(List videoIdList);
}
