package com.myoa.my_oa.controller;


import com.myoa.my_oa.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Api(tags = "首页接口")
@RestController
@RequestMapping("/my_oa/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;

}
