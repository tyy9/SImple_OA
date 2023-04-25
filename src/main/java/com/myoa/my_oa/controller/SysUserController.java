package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.Course;
import com.myoa.my_oa.entity.SysRole;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.entity.dto.SysMenu_father;
import com.myoa.my_oa.service.CourseService;
import com.myoa.my_oa.service.SysRoleService;
import com.myoa.my_oa.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-03-31
 */
@CrossOrigin
@Api(tags = "用户接口")
@RestController
@RequestMapping("/my_oa/sys-user")
public class  SysUserController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    CourseService courseService;
    @ApiOperation(value = "查询所有的用户")
    @GetMapping("/findAlluser")
    public R findAll(){
        List<SysUser> list = sysUserService.list(null);
        return R.ok().data("data",list);
    }

    @ApiOperation(value = "用户分页查询")
    @PostMapping("/pageUser/{page}/{limit}")
    public R pageUser(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable  int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @ApiParam(name = "sysUser",value = "用户对象")
            @RequestBody(required = false) SysUser sysUser
    ){
        Page<SysUser> sysUserPage = new Page<>(page,limit);
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.like(!StringUtils.isEmpty(sysUser.getUsername()),SysUser::getUsername,sysUser.getUsername())
                .orderByDesc(SysUser::getId);
        sysUserService.page(sysUserPage,sysUserLambdaQueryWrapper);
        long total = sysUserPage.getTotal();
        List<SysUser> records = sysUserPage.getRecords();
        return R.ok().data("total",total).data("data",records);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/addUser")
    public R addUser(
            @ApiParam(name = "user",value = "用户对象")
            @RequestBody SysUser user
    ){
        boolean save = sysUserService.save(user);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据用户id回显")
    @PostMapping("/findUserById/{id}")
    public R findUserById(
            @ApiParam(name = "id",value = "用户id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getId,id);
        SysUser one = sysUserService.getOne(sysUserLambdaQueryWrapper);
        return R.ok().data("user",one);
    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping("/updateUser")
    public R updateUser(
            @ApiParam(name = "user",value = "用户对象")
            @RequestBody  SysUser sysUser
    ){

        boolean b = sysUserService.updateById(sysUser);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/deleteUser/{id}")
    public R deleteUser(
            @ApiParam(name = "id",value = "用户id")
            @PathVariable  Integer id
    ){
        boolean b = sysUserService.removeById(id);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "批量删除用户信息")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name="ids",value = "用户id集合")
            @RequestBody List<Integer> ids
    ){
        boolean b = sysUserService.removeByIds(ids);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(
            @ApiParam(name="sysUser",value = "用户对象")
            @RequestBody SysUser sysUser
    ){
//        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        sysUserLambdaQueryWrapper.eq(SysUser::getUsername,sysUser.getUsername())
//                .eq(SysUser::getPassword,sysUser.getPassword());
//        SysUser one = sysUserService.getOne(sysUserLambdaQueryWrapper);
//        if(one!=null){
//            return R.ok();
//        }else{
//            return R.error().message("登录失败");
//        }
        String token = sysUserService.Login(sysUser);
        return R.ok().data("token",token);
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R register(
            @ApiParam(name = "user",value = "用户对象")
            @RequestBody SysUser user
    ){
        user.setRole("ROLE_ADMIN");
        boolean save = sysUserService.save(user);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "学生||普通用户注册")
    @PostMapping("/register_common")
    public R register_common(
            @ApiParam(name = "user",value = "用户对象")
            @RequestBody SysUser user
    ){
        user.setRole("ROLE_STUDENT");
        boolean save = sysUserService.save(user);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据token值获取用户信息")
    @GetMapping("/getToken")
    public R getToken(HttpServletRequest request){
        //检查是否能从请求头部获取token值
        boolean b = JwtUtils.checkToken(request);
        System.out.println(b);
        if(b) {
            //通过token值获取id值
            Integer memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
            System.out.println("token="+memberIdByJwtToken);
//            Integer id = Integer.parseInt(memberIdByJwtToken);
            //根据id值获取用户信息
            SysUser byId = sysUserService.getById(memberIdByJwtToken);
            return R.ok().data("user", byId);
        }else{
         return R.error().message("无用户信息，使用更多服务请登录").code(21);
        }
    }

    @ApiOperation(value = "根据用户权限获取菜单列表")
    @PostMapping("/getUserMenu")
    public R getUserMenu(
            @ApiParam(name="sysUser",value = "用户信息")
            @RequestBody SysUser sysUser
    ){
        List<SysMenu_father> menuList = sysUserService.getMenuList(sysUser);
        return R.ok().data("menulist",menuList);
    }

    @ApiOperation(value = "根据用户权限获取用户信息")
    @PostMapping("/getUserByRole/{role}")
    public R getUserByRole(
            @ApiParam(name="role",value = "角色权限")
            @PathVariable String role
    ){
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getRole,role);
        List<SysUser> list = sysUserService.list(sysUserLambdaQueryWrapper);
        return R.ok().data("userlist",list);
    }

    @ApiOperation(value = "首页老师展示数据")
    @GetMapping("/getIndexTeacher")
    @Cacheable(value = "index_teacher",key = "'teacher'")
    public R getIndexTeacher(){
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getRole,"ROLE_TEACHER").last("limit 6");
        List<SysUser> list = sysUserService.list(sysUserLambdaQueryWrapper);


        return R.ok().data("teacher",list);
    }

    @ApiOperation(value = "根据课程id获取老师信息")
    @PostMapping("/getTeacherByCourseId/{id}")
    public R getTeacherByCourseId(
            @ApiParam(value = "id",name = "课程id")
            @PathVariable Integer id
    ){
        Course byId = courseService.getById(id);
        Integer teacherId = byId.getTeacherId();
        SysUser teacher  = sysUserService.getById(teacherId);
        return  R.ok().data("teacher",teacher);
    }
}

