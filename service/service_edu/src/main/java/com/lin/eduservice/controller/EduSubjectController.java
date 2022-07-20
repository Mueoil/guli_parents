package com.lin.eduservice.controller;


import com.lin.R;
import com.lin.eduservice.pojo.subject.OneSubject;
import com.lin.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-11
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

//    添加课程分类
//    不能直接写路径，获取上传过来的文件，把文件中的内容读出来
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
//        上传过来的Excel文件 MultipartFile

        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();

    }



//    课程分类列表（树形）
    @GetMapping("/getALLSubject")
    public R getALLSubject(){
        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

