package com.myoa.my_oa.service;

import com.myoa.my_oa.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myoa.my_oa.entity.dto.SysMenu_father;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-05
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu_father> getmenu();

}
