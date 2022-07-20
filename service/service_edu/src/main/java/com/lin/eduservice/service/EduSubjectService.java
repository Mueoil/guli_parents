package com.lin.eduservice.service;

import com.lin.eduservice.pojo.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.eduservice.pojo.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-11
 */
public interface EduSubjectService extends IService<EduSubject> {

//    添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    //    课程分类列表（树形）
    List<OneSubject> getAllOneTwoSubject();
}
