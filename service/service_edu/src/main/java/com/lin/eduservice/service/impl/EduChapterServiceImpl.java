package com.lin.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.eduservice.pojo.EduChapter;
import com.lin.eduservice.mapper.EduChapterMapper;
import com.lin.eduservice.pojo.EduVideo;
import com.lin.eduservice.pojo.chapter.ChapterVo;
import com.lin.eduservice.pojo.chapter.VideoVo;
import com.lin.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.eduservice.service.EduVideoService;
import com.lin.exceptionHandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-19
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService eduVideoService;
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

//        1、根据课程id查询课程所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

//        2、根据课程id查询章节的所有小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> listVideoList= eduVideoService.list(wrapperVideo);

//        创建list集合，用于最终封装的数据
        ArrayList<ChapterVo> finalist = new ArrayList<>();

//        3、遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
//            得到每个章节
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalist.add(chapterVo);
            ArrayList<VideoVo> videoList = new ArrayList<>();
//        4、便利查询小节list集合进行封装
            for (int j = 0; j < listVideoList.size(); j++) {
                EduVideo eduVideo = listVideoList.get(j);
                if(chapterVo.getId().equals(eduVideo.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }
        }
            chapterVo.setChildren(videoList);
        }


        return finalist;
    }

//    删除章节
    @Override
    public Boolean deleteChapter(String chapterId) {
//        根据chapterId查询章节，看看章节内是否有小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        EduChapter eduChapter = baseMapper.selectById(chapterId);
        int count = eduVideoService.count(wrapper);
        if(count > 0){  //能查询出小节，不进行删除
            throw new GuliException(20001,"章节存在小节，不能删除");
        }else {
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
