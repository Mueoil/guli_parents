package com.lin.statistics.controller;


import com.lin.R;
import com.lin.statistics.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-14
 */
@RestController
//@CrossOrigin
@RequestMapping("/statistics/statistics-daily")
public class StatisticsDailyController {
    @Autowired
    StatisticsDailyService statisticsDailyService;

//    统计某一天的注册人数
    @PostMapping("/registerCount/{day}")
    public R  registerCount(@PathVariable String day){
        statisticsDailyService.registerCount(day);
        return R.ok();
    }

//    图表显示，返回两部分数据，日期Json数据，数量Json数据
    @GetMapping("/showData/{type}/{start}/{end}")
    public R showData(@PathVariable String type,@PathVariable String start,@PathVariable String end){
        Map<String,Object> map = statisticsDailyService.showData(type,start,end);
        return R.ok().data(map);
    }
}

