package com.lin.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.R;
import com.lin.eduservice.pojo.EduComment;
import com.lin.eduservice.service.EduCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-12
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduservice/edu-comment")
public class EduCommentController {

    @Autowired
    EduCommentService eduCommentService;

//    分页查询课程评论
    @GetMapping("/pageComment/{current}/{limit}")
    public R pageComment(@PathVariable long current,@PathVariable long limit,String courseId){
        Page<EduComment> page = new Page<>(current, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        wrapper.eq("course_id",courseId);
        eduCommentService.page(page,wrapper);
        List<EduComment> records = page.getRecords();
        long current1 = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();
        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("records",records);
        map.put("current",current1);
        map.put("pages",pages);
        map.put("size",size);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        return R.ok().data(map);
    }

//    添加评论
//     TODO
}

