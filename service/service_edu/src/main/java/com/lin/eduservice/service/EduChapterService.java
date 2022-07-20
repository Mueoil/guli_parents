package com.lin.eduservice.service;

import com.lin.eduservice.pojo.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.eduservice.pojo.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-19
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    Boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
