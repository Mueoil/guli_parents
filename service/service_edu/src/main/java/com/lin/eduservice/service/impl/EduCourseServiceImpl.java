package com.lin.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.eduservice.pojo.EduCourse;
import com.lin.eduservice.mapper.EduCourseMapper;
import com.lin.eduservice.pojo.EduCourseDescription;
import com.lin.eduservice.pojo.vo.CourseFrontVo;
import com.lin.eduservice.pojo.vo.CourseInfoVo;
import com.lin.eduservice.pojo.vo.CoursePublishVo;
import com.lin.eduservice.pojo.vo.CourseWebVo;
import com.lin.eduservice.service.EduChapterService;
import com.lin.eduservice.service.EduCourseDescriptionService;
import com.lin.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.eduservice.service.EduVideoService;
import com.lin.exceptionHandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-19
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    EduCourseMapper eduCourseMapper;
//    为了删除而注入
    @Autowired
    EduChapterService eduChapterService;
    @Autowired
    EduVideoService eduVideoService;
    //    添加课程信息的基本方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
//        向课程表添加课程的基本信息
//        CourseInfoVo 转为 EduCourse，因为这个是Course的service方法
        EduCourse eduCourse = new EduCourse();
//        先get出来，在set进去，这样就能获取得到courseInfoVo 的属性了
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int rows = baseMapper.insert(eduCourse);
        if(rows<=0){
            throw new GuliException(20001,"添加课程信息失败");
        }

//        获取添加之后的课程ID；
        String cid = eduCourse.getId();

//        向描述表(edu_course_description)中添加课程简介
//        因为这个是Course，我们要加入到描述表，所以用谁就注入谁
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(cid);
        eduCourseDescriptionService.save(eduCourseDescription);
        return cid;
    }

    @Override
    public CourseInfoVo getCouseInfo(String courseId) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();

//        查询课程信息涉及到两张表
//        先查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
//        再查询描述表
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());


        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
//        修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int row = baseMapper.updateById(eduCourse);
        if(row<=0){
            throw new GuliException(20001,"修改课程信息失败");
        }

//        修改描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoVo.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(courseDescription);
    }


    //    根据课程id查询课程的确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        System.out.println(1111111);
        CoursePublishVo publishCourseInfo = eduCourseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    @Override
    public void removeCourse(String courseId) {
//        1.根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);
//        2.根据课程id删除章节
        eduChapterService.removeChapterByCourseId(courseId);
//        3.根据课程id删除描述
        eduCourseDescriptionService.removeById(courseId);
//        4.删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result ==0 ){
            throw new GuliException(20001,"删除课程失败");
        }
    }

    @Cacheable(value = "course",key = "'selectCourseList'")
    @Override
    public List<EduCourse> listCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public Map getCourseFrontList(Page<EduCourse> page, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
//        判断条件值是否为空，不为空拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            wrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){
            wrapper.orderByDesc("price");
        }
        baseMapper.selectPage(page,wrapper);
        List<EduCourse> records = page.getRecords();
        long current = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();
        Map<String, Object> map = new HashMap<>();
        map.put("records",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return  baseMapper.getBaseCourseInfo(courseId);
    }
}
