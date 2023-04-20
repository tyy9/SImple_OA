package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.mapper.SysMenuMapper;
import com.myoa.my_oa.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-05
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu_father> getmenu() {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //先找出所有父菜单,pid为空
        sysMenuLambdaQueryWrapper.isNull(SysMenu::getPid);
        List<SysMenu> f_list = this.list(sysMenuLambdaQueryWrapper);
        //找出所有子菜单并放入父菜单,pid不为空
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper1.isNotNull(SysMenu::getPid);
        List<SysMenu> c_list = this.list(sysMenuLambdaQueryWrapper1);
        //创建树形菜单集合
        List<SysMenu_father> sysMenu_fathers = new ArrayList<SysMenu_father>();
        for(SysMenu s:f_list){
            //将找到的父菜单集合实例化成对象
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
            for(SysMenu s2:c_list){
                if(s2.getPid()==s.getId()){
                    sysMenus_child.add(s2);
                }
            }
            sysMenu_father.setChildren(sysMenus_child);
            sysMenu_fathers.add(sysMenu_father);
        }
        return sysMenu_fathers;
    }


}
