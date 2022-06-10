package com.atguigu.eduservice.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

//EasyExcel监听器，用来读excel表格
public class ExcelListener extends AnalysisEventListener<DemoData> {
    //一行一行读取excel内容  表头不会读取
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("****************"+demoData);

    }
    //读取完成之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
    //读取表头
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }
}
