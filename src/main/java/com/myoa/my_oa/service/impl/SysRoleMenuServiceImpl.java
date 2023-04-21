package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.SysRoleMenu;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.mapper.SysRoleMenuMapper;
import com.myoa.my_oa.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-06
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {


}
