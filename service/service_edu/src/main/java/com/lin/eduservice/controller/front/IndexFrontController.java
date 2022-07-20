package com.lin.eduservice.controller.front;

import com.lin.R;
import com.lin.eduservice.pojo.EduCourse;
import com.lin.eduservice.pojo.EduTeacher;
import com.lin.eduservice.service.EduCourseService;
import com.lin.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
//@CrossOrigin
public class IndexFrontController {
    @Autowired
    EduCourseService eduCourseService;
    @Autowired
    EduTeacherService eduTeacherService;

    @GetMapping("/index")
    public R index(){
        List<EduCourse> courseList = eduCourseService.listCourse();
        List<EduTeacher> teacherList = eduTeacherService.listTeacher();
        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }

}
