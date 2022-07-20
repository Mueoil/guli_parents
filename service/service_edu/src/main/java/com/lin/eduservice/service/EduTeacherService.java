package com.lin.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.eduservice.pojo.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author 有鱼
 * @since 2022-05-19
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> listTeacher();

    Map<String, Object> getTeacherFrontList(Page<EduTeacher> page);
}
