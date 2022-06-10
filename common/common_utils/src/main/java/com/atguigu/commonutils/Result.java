package com.atguigu.commonutils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

// 统一结果返回
@Data
public class Result {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "状态码 20000-成功 20001-失败")
    private Integer code;
    @ApiModelProperty(value = "错误原因")
    private String message;
    @ApiModelProperty(value = "返回的数据")
    private Map<String,Object> data = new HashMap<String,Object>();
    // 私有的无参构造函数，不能直接new ，只能调用类里面的静态方法
    private Result(){}
    @ApiModelProperty(value = "成功")
    public static Result success(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("成功");

        return result;
    }
    @ApiModelProperty(value = "成功")
    public static Result success(String message){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage(message);

        return result;
    }
    @ApiModelProperty(value = "成功")
    public static Result success(String message,Integer code){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage(message);

        return result;
    }
    @ApiModelProperty(value = "失败")
    public static Result error(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("失败");
        return result;
    }
    @ApiModelProperty(value = "失败")
    public static Result error(String message){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage(message);
        return result;
    }
    @ApiModelProperty(value = "失败")
    public static Result error(Integer code,String message){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    @ApiModelProperty(value = "添加数据")
    public Result data(String key,Object value){
        this.data.put(key,value);
        return this;
    }
    @ApiModelProperty(value = "添加Map数据")
    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }
}
