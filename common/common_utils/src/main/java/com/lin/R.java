package com.lin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//统一返回结果类
@Data
public class R {
    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("返回码")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("返回数据")
    private Map<String,Object> data = new HashMap<String,Object>();

//    构造器私有
    private R(){

    }

//    返回this是为了实现链式编程
//    成功静态方法
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(20000);
        r.setMessage("成功");
        return r;
    }
//    失败静态方法
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(20001);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }
    public R data(String key,Object value){
        this.data.put(key, value);
        return this;
    }
    public R data(Map<String,Object> map){
        this.setData(map);
        return this;
    }
}
