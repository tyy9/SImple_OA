package com.myoa.my_oa.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class R {
    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回信息")
    private String message;
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "返回信息")
    private Map<String,Object> data=new HashMap<>();

    //创建链式结构
    private R(){

    }

    public static  R ok(){
        R r=new R();
        r.setCode(20001);
        r.setMessage("成功");
        r.setSuccess(true);
        return r;
    }

    public static R error(){
        R r=new R();
           r.setCode(20000);
           r.setMessage("失败");
           r.setSuccess(false);
           return r;
    }

    public R code(Integer code){
        this.code=code;
        return  this;
    }
    public R message(String message){
        this.message=message;
        return  this;
    }

    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public R data(Map<String,Object>map){
        this.data=map;
        return this;
    }
}
