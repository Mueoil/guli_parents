package com.lin.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.lin.R;
import com.lin.exceptionHandler.GuliException;
import com.lin.vod.service.VodService;
import com.lin.vod.utils.ConstantVodUtils;
import com.lin.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {
    @Autowired
    VodService vodService;

    @PostMapping("/uploadAliyunVideo")
    public R uploadAliyunVideo(MultipartFile file){
//        因为我们需要返回视频id
        String videoId = vodService.uploadVideoAliyun(file);
        return R.ok().data("videoId",videoId);
    }

//    根据视频ID查询视频名称
//    @GetMapping("/selectVideoName/{videoId}")
//    public R selectVideoName(@PathVariable String videoId){
//        QueryWrapper<Object> wrapper = new QueryWrapper<>();
//        wrapper.eq("video_source_id",videoId);
//        wrapper.select("video_original_name");
//        vodService.
//        return R.ok();
//    }

//    根据视频Id 删除OSS中的视频
    @DeleteMapping("/removeAliyun/{id}")
    public R removeAliyunVideo(@PathVariable String id){

        try {
            String access_key_id = ConstantVodUtils.Access_KEY_ID;
            String access_key_secret = ConstantVodUtils.Access_KEY_SECRET;
//            初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(access_key_id, access_key_secret);
//            创建一个删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
//            向request设置视频id
            request.setVideoIds(id);
//            调用初始化对象的方法，实现删除
            client.getAcsResponse(request);     //因为是删除不需要返回response对象
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }

    }

//    删除OSS多个视频的方法，所以传递多个视频id，List存放
    @DeleteMapping("/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAliyunVideo(videoIdList);
        return R.ok();
    }

//    根据视频id获取视频凭证
    @GetMapping("/getPlayAuth/{videoId}")
    public R getPlayAuth(@PathVariable String videoId) throws ClientException {
        String access_key_id = ConstantVodUtils.Access_KEY_ID;
        String access_key_secret = ConstantVodUtils.Access_KEY_SECRET;
        DefaultAcsClient client = InitVodClient.initVodClient(access_key_id, access_key_secret);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
        String playAuth = response.getPlayAuth();
        return R.ok().data("playAuth",playAuth);
    }
}
