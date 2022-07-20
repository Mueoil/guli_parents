package com.lin.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.eduservice.pojo.EduSubject;
import com.lin.eduservice.pojo.excel.SubjectData;
import com.lin.eduservice.service.EduSubjectService;
import com.lin.exceptionHandler.GuliException;

public class SubjectExecleListener extends AnalysisEventListener<SubjectData> {

//    因为SubjectExecleListener不能交给spring进行管理，需要自己new，不能注入其他对象
//    不能实现mapper.   service.
//    解决：手动有参构造，即增加一个参数EduSubjectService subjectService
    public EduSubjectService subjectService;
    public SubjectExecleListener() {}
    public SubjectExecleListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }



//  读取excel内容，一行一行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null){
            throw new GuliException(20001,"文件数据为空");
        }

//        一行一行读取，每次读取2个值，第一个值一级分类，第二个值二级分类
//        先判断一级分类是否重复
        EduSubject exitOneSubject = this.exitOneSubject(subjectData.getOneSubjectName(), subjectService);
        if(exitOneSubject == null){ //  没有相同的一级分类,进行添加
            exitOneSubject = new EduSubject();
            exitOneSubject.setParentId("0");
            exitOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(exitOneSubject);

        }

//        获取一级分类的id值
        String pid = exitOneSubject.getId();
//        先判断二级分类是否重复
        EduSubject exitTwoSubject = this.exitTwoSubject(subjectData.getTwoSujectName(),pid, subjectService);
        if(exitTwoSubject == null){ //  没有相同的一级分类,进行添加
            exitTwoSubject = new EduSubject();
            exitTwoSubject.setParentId(pid);
            exitTwoSubject.setTitle(subjectData.getTwoSujectName());
            subjectService.save(exitTwoSubject);

        }

    }

//    判断一级分类不能重复添加
    private EduSubject exitOneSubject(String name,EduSubjectService eduSubjectService){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

//    判断二级分类不能重复添加
    private EduSubject exitTwoSubject(String name,String pid,EduSubjectService eduSubjectService){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
