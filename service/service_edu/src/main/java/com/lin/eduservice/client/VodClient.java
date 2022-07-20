package com.lin.eduservice.client;

import com.lin.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClien.class)

public interface VodClient {

//    定义调用方法的路径
//    直接把Controller的方法复制过来
        @DeleteMapping("/eduvod/video/removeAliyun/{id}")
        public R removeAliyunVideo(@PathVariable("id") String id); //@PathVariable("id") 一定要写


//        调用vod的删除多个视频方法
        @DeleteMapping("/eduvod/video/delete-batch")
        public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);

}
