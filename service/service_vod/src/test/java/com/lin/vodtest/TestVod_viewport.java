package com.lin.vodtest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;

/**
 * 根据视频ID获取视频凭证
 */
public class TestVod_viewport {
    public static void main(String[] args) throws ClientException {
//        String accessKeyId = "LTAI5tKP7BEWXrMjnyXkstqG";
//        String accessKeySecret = "WNUDeEfg2NpOKbAfG2rabdcFRtc9P4";
//        String title = "6 - What If I Want to Move Faster.mp4 - upload by sdk";      //上传之后文件的名称
//        String fileName = "D:/Maven/实战项目/在线教育/资料/1-阿里云上传测试视频/6 - What If I Want to Move Faster.mp4";   //本地文件路径和名称
//
////        实现上传视频的方法
//        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
//
//        request.setPartSize(2 * 1024 * 1024L);
//        request.setTaskNum(1);
//        UploadVideoImpl uploader = new UploadVideoImpl();
//        UploadVideoResponse response = uploader.uploadVideo(request);
////        System.out.print("RequestId=" + response.getRequestId() + "\n"); //请求视频点播服务的请求ID
//        if (response.isSuccess()) {     //不是判断是否上传成功，而是判断是否有回调值
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//        } else {
//            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//            System.out.print("ErrorCode=" + response.getCode() + "\n");
//            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
//        }
        getPlayAuth();
    }

    public static void getPlayAuth() throws ClientException {
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tKP7BEWXrMjnyXkstqG", "WNUDeEfg2NpOKbAfG2rabdcFRtc9P4");
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        request.setVideoId("dc4cf635bb5d42bbb4ff71e54aca950a");
        response = client.getAcsResponse(request);
        System.out.print("playauth:"+response.getPlayAuth());
    }
}


