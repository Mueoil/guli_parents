package com.lin.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.eduservice.listener.SubjectExecleListener;
import com.lin.eduservice.pojo.EduSubject;
import com.lin.eduservice.mapper.EduSubjectMapper;
import com.lin.eduservice.pojo.excel.SubjectData;
import com.lin.eduservice.pojo.subject.OneSubject;
import com.lin.eduservice.pojo.subject.TwoSubject;
import com.lin.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author 有鱼
 * @since 2022-06-11
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


//    添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExecleListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
//        查询所有的一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubejctList = baseMapper.selectList(wrapperOne);

//        查询所有的二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> twoSubejctList = baseMapper.selectList(wrapperTwo);

//        创建一个list集合List<OneSubject>，用于存储最终封装的数据
//        里面放什么？封装一级分类+封装二级分类   要把一级二级的集合变成List<OneSubject>
        List<OneSubject> finalSubjectList = new ArrayList<>();

//        封装一级分类
//        查询出来的所有的一级分类list集合遍历，得到每一个一级分类对象，获取每一个一级分类对象的值
//        然后再封装List<OneSubject>
        for (int i = 0; i < oneSubejctList.size(); i++) {
//            得到OneSubejctList每个EduSubject对象
            EduSubject eduSubject = oneSubejctList.get(i);

//            把eduSubject里面的值获取出来，放到OneSubject对象里面
//            多个OneSubject再放到finalSubjectList里面
            OneSubject oneSubject = new OneSubject();
//            把eduSubject的get方法，set到oneSubject里面   eg oneSubject.setId(eduSubject.getId());
            BeanUtils.copyProperties(eduSubject,oneSubject);
            finalSubjectList.add(oneSubject);




//            在一级分类循环便利所有的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for (int j = 0;  j< twoSubejctList.size(); j++) {
                EduSubject tSubject = twoSubejctList.get(j);
//                判断二级分类的parent_id和一级分类的id是否一样
                if (tSubject.getParentId().equals(eduSubject.getId())) {
//                    把tSubject复制到TwoSubject
//                    再把TwoSubject放到twoFinalSubjectList
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }

//            最终：把一级下面所有的二级放到一级类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }


//        封装二级分类
        return finalSubjectList;
    }
}
