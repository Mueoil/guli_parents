package com.lin.statistics.schedule;

import com.lin.statistics.service.StatisticsDailyService;
import com.lin.statistics.uitls.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    StatisticsDailyService statisticsDailyService;

//    每个5s执行
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("task1");
//    }

    //    每个凌晨1.查询，把前一天数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2(){
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));

    }
}
