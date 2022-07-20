package com.lin.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.JwtUtils;
import com.lin.R;
import com.lin.eduservice.client.OrderClient;
import com.lin.eduservice.pojo.EduCourse;
import com.lin.eduservice.pojo.chapter.ChapterVo;
import com.lin.eduservice.pojo.vo.CourseFrontVo;
import com.lin.eduservice.pojo.vo.CourseWebVo;
import com.lin.eduservice.service.EduChapterService;
import com.lin.eduservice.service.EduCourseService;
import com.lin.exceptionHandler.GuliException;
import com.lin.ordervo.CourseWebVoOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduChapterService eduChapterService;

    @Autowired
    OrderClient orderClient;

//    条件查询带分页查询课程
    @PostMapping("/getFrontCourseList/{current}/{limit}")
    public R getFrontCourseList(@PathVariable long current, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> page = new Page<>(current,limit);
        Map map = eduCourseService.getCourseFrontList(page,courseFrontVo);
        return R.ok().data(map);
    }


//    课程详情的方法
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
//        多表查询，编写sql语句查询课程信息
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfo(courseId);
//        根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);

//    根据课程id和用户id查询当前当前课程是否已经支付过了
//        memberId通过token获取
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(memberId==null){
            throw new GuliException(20001,"必须登陆后才可浏览课程详情信息");
        }
        Boolean buyCourse = orderClient.isBuyCourse(courseId, memberId);

        return R.ok().data("courseWeVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }


    


//    根据课程id查询课程信息
    @PostMapping("/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo baseCourseInfo = eduCourseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(baseCourseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}
