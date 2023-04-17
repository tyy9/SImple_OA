package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.SysRole;
import com.myoa.my_oa.entity.SysRoleMenu;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.mapper.SysUserMapper;
import com.myoa.my_oa.service.SysMenuService;
import com.myoa.my_oa.service.SysRoleMenuService;
import com.myoa.my_oa.service.SysRoleService;
import com.myoa.my_oa.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyy9
 * @since 2023-03-31
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysMenuService sysMenuService;

    @Override
    public String Login(SysUser user) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUsername,user.getUsername())
                .eq(SysUser::getPassword,user.getPassword());
        SysUser one = this.getOne(sysUserLambdaQueryWrapper);
        if(one!=null){
            String token=JwtUtils.getJwtToken(one.getId(),one.getUsername());

            return token;
        }else{
            throw new CustomerException(20000,"登录失败，请检查你的用户名与密码");
        }
    }

    @Override
    public List<SysMenu_father> getMenuList(SysUser sysUser) {
        //根据用户权限获取角色对象
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getFlag,sysUser.getRole());
        SysRole role = sysRoleService.getOne(sysRoleLambdaQueryWrapper);
        //根据角色对象id获取菜单列表
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId,role.getId());
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

        return rolemenu_total;
    }
}
