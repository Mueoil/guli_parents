package com.lin.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.R;
import com.lin.eduservice.pojo.EduTeacher;
import com.lin.eduservice.pojo.vo.TeacherQuery;
import com.lin.eduservice.service.EduTeacherService;
import com.lin.exceptionHandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-05-19
 */
@RestController
@RequestMapping("/eduservice/edu-teacher")
//解决跨域问题，加一个注解
//@CrossOrigin

@Api("讲师管理")

public class EduTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;

//    1.查询所有讲师表的所有操作
    @GetMapping("/findAll")
    @ApiOperation("所有讲师列表")
    public R findAllTeacher(){
        try {
//            int i = 1/0;
        } catch (Exception e) {
            throw new GuliException(20001,"执行了自定义异常处理");
        }
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

//    2.逻辑删除讲师的方法
    @DeleteMapping("{id}")
//    也可以swagger中所需的参数添加描述@ApiParam
    @ApiOperation("逻辑删除讲师")
    public R removeTeacher(@PathVariable String id){
        boolean flag = eduTeacherService.removeById(id);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }


//    3.分页查询讲师方法
    @ApiOperation("分页查询讲师方法")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);    //当前1页，每页3个记录
//        调用方法时，底层封装，把分页所有数据封装到pageTeacher对象里面
        eduTeacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
    }


//    4.条件查询带分页
    @ApiOperation("条件查询带分页")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,@PathVariable long limit,
                                 @RequestBody(required = false) TeacherQuery teacherQuery){
//           用了@RequestBody，就只能用post提交方式 false 表示可以为空

        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
//        多条件组合查询
//        类似mybatis的动态SQL
//        判断条件值是否为空，如果不为空拼接查询条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
//            构建条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
//            构建条件
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
//            构建条件
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
//            构建条件
            wrapper.le("gmt_modified",end);
        }
//        排序
        wrapper.orderByDesc("gmt_create");
        eduTeacherService.page(pageTeacher,wrapper);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }


//    5.添加讲师接口的方法
    @PostMapping("/addTeacher")
    @ApiOperation("添加讲师接口的方法")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }


//    6.修改讲师接口的方法
//    先查询，在修改
    @GetMapping("/getTeacher/{id}")
    @ApiOperation("根据ID查询讲师")
    public R getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);

    }

    @ApiOperation("修改讲师操作")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){

        boolean falg = eduTeacherService.updateById(eduTeacher);
        if(falg){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

