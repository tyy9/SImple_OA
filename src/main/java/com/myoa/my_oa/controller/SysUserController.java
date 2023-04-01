package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-03-31
 */
@CrossOrigin
@Api(tags = "用户接口")
@RestController
@RequestMapping("/my_oa/sys-user")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;
    @ApiOperation(value = "查询所有的用户")
    @GetMapping("/findAlluser")
    public R findAll(){
        List<SysUser> list = sysUserService.list(null);
        return R.ok().data("data",list);
    }

    @ApiOperation(value = "用户分页查询")
    @PostMapping("/pageUser/{page}/{limit}")
    public R pageUser(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable  int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @RequestBody(required = false) SysUser sysUser
    ){
        Page<SysUser> sysUserPage = new Page<>(page,limit);
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.like(!StringUtils.isEmpty(sysUser.getUsername()),SysUser::getUsername,sysUser.getUsername())
                .orderByDesc(SysUser::getId);
        sysUserService.page(sysUserPage,sysUserLambdaQueryWrapper);
        long total = sysUserPage.getTotal();
        List<SysUser> records = sysUserPage.getRecords();
        return R.ok().data("total",total).data("data",records);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/addUser")
    public R addUser(
            @ApiParam(name = "user",value = "用户对象")
            @RequestBody SysUser user
    ){
        boolean save = sysUserService.save(user);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据用户id回显")
    @PostMapping("/findUserById/{id}")
    public R findUserById(
            @ApiParam(name = "id",value = "用户id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getId,id);
        SysUser one = sysUserService.getOne(sysUserLambdaQueryWrapper);
        return R.ok().data("user",one);
    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping("/updateUser")
    public R updateUser(
            @ApiParam(name = "user",value = "用户对象")
            SysUser sysUser
    ){
        boolean b = sysUserService.updateById(sysUser);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/deleteUser/{id}")
    public R deleteUser(
            @ApiParam(name = "id",value = "用户id")
            @PathVariable  Integer id
    ){
        boolean b = sysUserService.removeById(id);
        return b?R.ok():R.error();
    }
}

