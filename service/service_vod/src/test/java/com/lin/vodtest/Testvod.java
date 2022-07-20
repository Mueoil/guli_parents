package com.lin.vodtest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

/**
 * 根据视频ID获取视频地址
 */
public class Testvod {
    public static void main(String[] args) throws ClientException {
        getPlayUrl();
    }
    public static void getPlayUrl() throws ClientException {
        //        根据视频ID获取视频的播放地址
//        1.创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tKP7BEWXrMjnyXkstqG", "WNUDeEfg2NpOKbAfG2rabdcFRtc9P4");
//        2.创建获取视频地址的request、response对象
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
//        3.向request对象里面设置视频的id值
        request.setVideoId("44ab296d049a43fb9234e41e529f7279");
//        4.调用初始化对象里面的方法传递request，最终获取数据
        response = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
}
