package com.atguigu.eduservice.excel;

import com.alibaba.excel.EasyExcel;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
/*
        //实现excel写操作
        //1、设置写入文件夹地址和excel文件名称
        String filename = "D:\\write.xlsx";
        //2、调用easyexcel里面的方法实现写操作
        //第一个参数：文件路径名称， 第二个参数：实体类.class
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
*/


        //读操作
        String filename = "D:\\write.xlsx";
        // 第一个参数： 读取的文件路径  第二个参数：实体类对象.class  第三个参数：EasyExcel监听器
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }
    //创建方法啊，返回list集合
    private  static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("张三 ---->" +i);
            list.add(demoData);
        }
        return list;
    }
}
