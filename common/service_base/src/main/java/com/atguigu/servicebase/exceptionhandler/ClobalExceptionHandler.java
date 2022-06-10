package com.atguigu.servicebase.exceptionhandler;


import com.atguigu.commonutils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j //
public class ClobalExceptionHandler {
    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error("执行了全局异常处理....");
    }
    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.error("执行了特定异常....");
    }
    //特定异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public Result error(GuliException e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e)); //以error级打印到文件中
        return Result.error(e.getCode(),e.getMessage());
    }
}
