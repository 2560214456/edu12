package com.atguigu.eduservice.entity.Excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
// excel对应的实体类
@Data
public class SubjectData {
    @ExcelProperty(index = 0)
    private String oneSubjectName;
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
