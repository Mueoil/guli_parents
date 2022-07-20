package om.lin.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

//    读操作
    @Test
    public void read(){
//        实现excel的读操作
        String filename = "D:\\Maven\\实战项目\\easyexcel\\write.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }



    public static void main(String[] args) {
//        实现excel的写操作
//        1.设置写入文件夹的地址和excel文件名称
        String filename = "D:\\Maven\\实战项目\\easyexcel\\write.xlsx";

//        2.调用easyexcel里面的方法进行写操作
//        1参是文件路径的名称，2参是类的class
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
    }

//    创建方法返回List集合
    public static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("youyu"+i);
            list.add(data);
        }
        return list;
    }
}
