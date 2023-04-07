package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.SysRole;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@Api(tags = "菜单接口")
@RestController
@RequestMapping("/my_oa/sys-menu")
public class SysMenuController {
    @Autowired
    SysMenuService sysMenuService;
    @ApiOperation(value = "菜单查询接口")
    @GetMapping("/findMenu")
    public R pageMenu(){
        List<SysMenu_father> sysMenu_fathers = sysMenuService.getmenu();
        return R.ok().data("menu",sysMenu_fathers);
    }

    @ApiOperation(value = "菜单添加接口")
    @PostMapping("/addMenu")
    public R addMenu(
            @ApiParam(name="sysMenu",value = "菜单对象")
            @RequestBody SysMenu sysMenu
    ){
        boolean save = sysMenuService.save(sysMenu);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据菜单id回显")
    @PostMapping("/findMenuById/{id}")
    public R findRoleById(
            @ApiParam(name = "id",value = "菜单id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getId,id);
        SysMenu one = sysMenuService.getOne(sysMenuLambdaQueryWrapper);
        return R.ok().data("menu",one);
    }

    @ApiOperation(value = "更新菜单信息")
    @PostMapping("/updateMenu")
    public R updateRole(
            @ApiParam(name = "menu",value = "菜单对象")
            @RequestBody  SysMenu sysMenu
    ){

        boolean b = sysMenuService.updateById(sysMenu);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "删除菜单信息")
    @DeleteMapping("/deleteMenu/{id}")
    public R deleteUser(
            @ApiParam(name = "id",value = "菜单id")
            @PathVariable  Integer id
    ){
        boolean b = sysMenuService.removeById(id);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "批量删除菜单信息")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name="ids",value = "菜单id集合")
            @RequestBody List<Integer> ids
    ){
        boolean b = sysMenuService.removeByIds(ids);
        return b?R.ok():R.error();
    }
}

