package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.mapper.SysUserMapper;
import com.myoa.my_oa.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
