package com.lin.eduservice.controller;


import com.lin.R;
import com.lin.eduservice.client.VodClient;
import com.lin.eduservice.pojo.EduVideo;
import com.lin.eduservice.service.EduVideoService;
import com.lin.exceptionHandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-19
 */
@RestController
@RequestMapping("/eduservice/edu-video")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    EduVideoService eduVideoService;

//    注入VodClient
    @Autowired
    VodClient vodClient;
//    添加小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody  EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return  R.ok();
    }

//    删除小节
//    后期这个方法需要完善，，删除小节的时候，视频也需要删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){

//        根据小节id，得到视频id
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            R r = vodClient.removeAliyunVideo(videoSourceId);
            if(r.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败，熔断器原因...");
            }
        }

//        先删除视频，再删除小节
        eduVideoService.removeById(id);
        return R.ok();
    }

//    修改小节
//    1.根据章节id查询
    @GetMapping("/getVideoInfo/{chapter}")
    public R getVideoInfo(@PathVariable String chapter){
        EduVideo video = eduVideoService.getById(chapter);
        return R.ok().data("video",video);
    }

    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }
}

