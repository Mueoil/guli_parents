package com.lin.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.R;
import com.lin.statistics.UcenterClient.UcenterClient;
import com.lin.statistics.pojo.StatisticsDaily;
import com.lin.statistics.mapper.StatisticsDailyMapper;
import com.lin.statistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author 有鱼
 * @since 2022-07-14
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    UcenterClient ucenterClient;
    @Override
    public void registerCount(String day) {
//        添加记录之前删除表相同的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        R r = ucenterClient.countRegister(day);
        Integer count = (Integer) r.getData().get("count");
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(count);  //注册人数
        daily.setDateCalculated(day); //统计日期
        daily.setVideoViewNum(RandomUtils.nextInt(100,200));
        daily.setLoginNum(RandomUtils.nextInt(100,200));
        daily.setCourseNum(RandomUtils.nextInt(100,200));
        System.out.println(daily);
        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> showData(String type, String start, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",start,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> list = baseMapper.selectList(wrapper);
//          返回有两部分数据：日期和r日期对应数量
//          前端要求数组的Json格式
        List<String> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            StatisticsDaily statisticsList= list.get(i);
//            封装日期
            dateList.add(statisticsList.getDateCalculated());
//            封装数量
            if(type.equals("register_num")){
                countList.add(statisticsList.getRegisterNum());
            }
            if(type.equals("login_num")){
                countList.add(statisticsList.getLoginNum());
            }
            if(type.equals("video_view_num")){
                countList.add(statisticsList.getVideoViewNum());
            }
            if(type.equals("course_num")){
                countList.add(statisticsList.getCourseNum());
            }
        }
        Map map = new HashMap<>();
        map.put("date",dateList);
        map.put("count",countList);
        return map;
    }
}
