package com.lin.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.R;
import com.lin.eduservice.pojo.EduCourse;
import com.lin.eduservice.pojo.EduTeacher;
import com.lin.eduservice.service.EduCourseService;
import com.lin.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {
    @Autowired
    EduTeacherService eduTeacherService;
    @Autowired
    EduCourseService eduCourseService;
//    分页查询讲师
    @PostMapping("/getTeacherFrontList/{current}/{limit}")
    public R getTeacherFrontList(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> page = new Page<>(current,limit);
        Map<String,Object> map = eduTeacherService.getTeacherFrontList(page);
//        返回分页的所有数据
        return R.ok().data(map);

    }


//    讲师详情功能，讲师
    @GetMapping("/getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        EduTeacher teacher = eduTeacherService.getById(teacherId);
//     讲师详情功能，课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = eduCourseService.list(wrapper);
        return R.ok().data("teacherInfo",teacher).data("courseList",courseList);
    }

}
