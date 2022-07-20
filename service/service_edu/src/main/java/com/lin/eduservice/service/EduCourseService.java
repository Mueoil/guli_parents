package com.lin.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.eduservice.pojo.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.eduservice.pojo.vo.CourseFrontVo;
import com.lin.eduservice.pojo.vo.CourseInfoVo;
import com.lin.eduservice.pojo.vo.CoursePublishVo;
import com.lin.eduservice.pojo.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-19
 */
public interface EduCourseService extends IService<EduCourse> {




    //    添加课程信息的基本方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //    根据课程id查询课程的基本信息
    CourseInfoVo getCouseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //    根据课程id查询课程的确认信息
    CoursePublishVo publishCourseInfo(String id);


    //    删除课程
    void removeCourse(String courseId);

    List<EduCourse> listCourse();


    Map getCourseFrontList(Page<EduCourse> page, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}

