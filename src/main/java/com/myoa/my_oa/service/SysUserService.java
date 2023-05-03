package com.myoa.my_oa.service;

import com.myoa.my_oa.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myoa.my_oa.entity.dto.IndexLoginDto;
import com.myoa.my_oa.entity.dto.SysMenu_father;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyy9
 * @since 2023-03-31
 */
public interface SysUserService extends IService<SysUser> {
    String Login(SysUser user);

    List<SysMenu_father> getMenuList(SysUser sysUser);


    boolean register_student(IndexLoginDto indexLoginDto);
}
