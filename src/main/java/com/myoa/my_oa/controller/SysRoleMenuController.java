package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.SysRoleMenu;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.service.SysMenuService;
import com.myoa.my_oa.service.SysRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单关系表 前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-04-06
 */
@Api(tags = "角色菜单关系接口")
@CrossOrigin
@RestController
@RequestMapping("/my_oa/sys-role-menu")
public class SysRoleMenuController {
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysMenuService sysMenuService;
    @ApiOperation(value = "根据角色id寻找菜单接口")
    @PostMapping("/findMenuByRoleId/{id}")
    public R findMenuByRoleId(
            @ApiParam(name="id",value = "角色id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId,id);
        List<SysRoleMenu> list = sysRoleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        ArrayList<SysMenu_father> rolemenu_total = new ArrayList<>();
        for(SysRoleMenu srm:list){
            SysMenu s = sysMenuService.getById(srm.getMenuId());
            LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //把父菜单下的子菜单也找出来
            sysMenuLambdaQueryWrapper.eq(SysMenu::getPid,s.getId());
            List<SysMenu> list_menu = sysMenuService.list(sysMenuLambdaQueryWrapper);
            SysMenu_father sysMenu_father = new SysMenu_father();
            sysMenu_father.setDescription(s.getDescription());
            sysMenu_father.setIcon(s.getIcon());
            sysMenu_father.setId(s.getId());
            sysMenu_father.setName(s.getName());
            sysMenu_father.setPagePath(s.getPagePath());
            sysMenu_father.setSortNum(s.getSortNum());
            sysMenu_father.setPath(s.getPath());
            sysMenu_father.setPid(s.getPid());
            //创建子菜单集合，根据pid与id适配加入集合中
            List<SysMenu> sysMenus_child = new ArrayList<>();
            sysMenus_child=list_menu;
            sysMenu_father.setChildren(sysMenus_child);
            rolemenu_total.add(sysMenu_father);
        }
        return R.ok().data("menu_total",rolemenu_total);
    }


    @ApiOperation(value = "角色菜单信息添加")
    @PostMapping("/addRoleMenu")
    public R addRoleMenu(
            @ApiParam(name = "sysRoleMenu",value = "角色菜单对象")
            @RequestBody List<SysRoleMenu> sysRoleMenus
    ){

        //先将所有关系删除再重新添加
        sysRoleMenuService.removeById(sysRoleMenus.get(0).getRoleId());
        for(SysRoleMenu s:sysRoleMenus){
           sysRoleMenuService.save(s);
        }

        return  R.ok();
    }




}

