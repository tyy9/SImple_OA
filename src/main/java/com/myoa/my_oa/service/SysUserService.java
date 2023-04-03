package com.myoa.my_oa.service;

import com.myoa.my_oa.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
