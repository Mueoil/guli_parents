package com.lin.eduservice.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectData {

//    一级分类
//    index表示第几列 0 表示第一列   写index 的目的是是为了读操作

    @ExcelProperty(index = 0)
    private String oneSubjectName;

//    二级分类
    @ExcelProperty(index = 1)
    private String twoSujectName;
}
