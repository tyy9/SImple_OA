package com.myoa.my_oa.controller;


import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.dto.IndexLoginDto;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.service.SysUserService;
import com.myoa.my_oa.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@CrossOrigin
@Api(tags = "首页接口")
@RestController
@RequestMapping("/my_oa/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisTemplate redisTemplate;

    @ApiModelProperty(value = "注册验证码获取接口")
    @PostMapping("/getcode")
    public R getcode(
            @ApiParam(value = "indexlogindto",name = "注册对象")
            @RequestBody IndexLoginDto indexLoginDto
            ){
        //根据手机号获取验证码
        String username = indexLoginDto.getUsername();
        if(username!=null){
            Integer integer = ValidateCodeUtils.generateValidateCode(6);
            redisTemplate.opsForValue().set("code"+username,integer,1, TimeUnit.MINUTES);
            return R.ok();
        }else {
            throw new CustomerException(20000,"请填写手机号码");
        }
    }

    @ApiModelProperty(value = "学生注册接口")
    @PostMapping("/register_student")
    public R register_student(
            @ApiParam(value = "indexlogindto",name = "注册对象")
            @RequestBody IndexLoginDto indexLoginDto
    ){
        System.out.println(indexLoginDto);
        sysUserService.register_student(indexLoginDto);
        return R.ok();
    }


}
