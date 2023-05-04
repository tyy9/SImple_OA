package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.SysRole;
import com.myoa.my_oa.entity.SysRoleMenu;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.entity.dto.IndexLoginDto;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.mapper.SysUserMapper;
import com.myoa.my_oa.service.SysMenuService;
import com.myoa.my_oa.service.SysRoleMenuService;
import com.myoa.my_oa.service.SysRoleService;
import com.myoa.my_oa.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoa.my_oa.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String Login(SysUser user) {
        System.out.println(MD5.encrypt(user.getPassword()));
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUsername,user.getUsername())
                .eq(SysUser::getStatus,true)
                .eq(SysUser::getPassword,MD5.encrypt(user.getPassword()))
                .eq(SysUser::getRole,user.getRole());

        SysUser one = this.getOne(sysUserLambdaQueryWrapper);
        if(one!=null){
            String token=JwtUtils.getJwtToken(one.getId(),one.getUsername());

            return token;
        }else{
            throw new CustomerException(20000,"登录失败，请检查你的用户名,密码与权限,如果有冻结嫌疑请联系管理员");
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

    @Override
    public boolean register_student(IndexLoginDto indexLoginDto) {
        String username = indexLoginDto.getUsername();
        String nickname = indexLoginDto.getNickname();
        String password = indexLoginDto.getPassword();
        Integer code = indexLoginDto.getCode();
        if(username!=null&&nickname!=null&&password!=null){
            //检测重复
            LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserLambdaQueryWrapper.eq(SysUser::getUsername,username);
            SysUser one = this.getOne(sysUserLambdaQueryWrapper);
            if(one!=null){
                throw new CustomerException(20000,"用户名已被使用");
            }
            Integer code_redis =(Integer) redisTemplate.opsForValue().get("code" + username);
            System.out.println(code_redis);
            if(code_redis.equals(code)){
                String encrypt = MD5.encrypt(password);
                SysUser sysUser = new SysUser();
                sysUser.setUsername(username);
                sysUser.setPassword(encrypt);
                sysUser.setNickname(nickname);
                sysUser.setRole("ROLE_STUDENT");
                sysUser.setStatus(true);
                boolean save = this.save(sysUser);
                return save;

            }else{
                throw new CustomerException(20000,"验证码错误");
            }
        }else {
            throw new CustomerException(20000,"必填项不可为空");
        }
    }
}
