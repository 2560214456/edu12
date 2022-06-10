package com.atguigu.servicebase.exceptionhandler;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    public static String getMessage(Exception e){
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将错误的栈信息输出到PrintWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        }finally {
            if (sw != null){
                pw.close();
            }
        }
        return sw.toString();
    }
}
