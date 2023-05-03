package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.SysRole;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.service.SysRoleService;
import io.swagger.annotations.Api;
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
 * @since 2023-04-05
 */
@CrossOrigin
@Api(tags = "角色接口")
@RestController
@RequestMapping("/my_oa/sys-role")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;
    @ApiOperation(value = "角色分页查询")
    @PostMapping("/pageRole/{page}/{limit}")
    public R pageRole(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @ApiParam(name = "sysRole",value = "角色对象")
            @RequestBody(required = false) SysRole sysRole
    ){
        Page<SysRole> sysRolePage = new Page<>(page,limit);
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.like(!StringUtils.isEmpty(sysRole.getName()),SysRole::getName,sysRole.getName());
        sysRoleService.page(sysRolePage,sysRoleLambdaQueryWrapper);
        long total = sysRolePage.getTotal();
        List<SysRole> records = sysRolePage.getRecords();
        return R.ok().data("total",total).data("data",records);
    }
    @ApiOperation(value = "获取所有角色")
    @GetMapping("/getAllRole")
    public R getAllRole(
    ){
        List<SysRole> list = sysRoleService.list();
        return R.ok().data("data",list);
    }


    @ApiOperation(value = "新增角色")
    @PostMapping("/addRole")
    public R addRole(
            @ApiParam(name = "user",value = "角色对象")
            @RequestBody SysRole sysRole
    ){
        boolean save = sysRoleService.save(sysRole);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据角色id回显")
    @PostMapping("/findRoleById/{id}")
    public R findRoleById(
            @ApiParam(name = "id",value = "角色id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getId,id);
        SysRole one = sysRoleService.getOne(sysRoleLambdaQueryWrapper);
        return R.ok().data("role",one);
    }

    @ApiOperation(value = "更新角色信息")
    @PostMapping("/updateRole")
    public R updateRole(
            @ApiParam(name = "user",value = "用户对象")
            @RequestBody  SysRole sysRole
    ){

        boolean b = sysRoleService.updateById(sysRole);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "删除角色信息")
    @DeleteMapping("/deleteRole/{id}")
    public R deleteUser(
            @ApiParam(name = "id",value = "角色id")
            @PathVariable  Integer id
    ){
        boolean b = sysRoleService.removeById(id);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "批量删除角色信息")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name="ids",value = "角色id集合")
            @RequestBody List<Integer> ids
    ){
        boolean b = sysRoleService.removeByIds(ids);
        return b?R.ok():R.error();
    }
}

