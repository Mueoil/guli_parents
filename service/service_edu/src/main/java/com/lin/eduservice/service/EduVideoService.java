package com.lin.eduservice.service;

import com.lin.eduservice.pojo.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-19
 */
public interface EduVideoService extends IService<EduVideo> {

//    根据课程id删除小节
    void removeVideoByCourseId(String courseId);
}
