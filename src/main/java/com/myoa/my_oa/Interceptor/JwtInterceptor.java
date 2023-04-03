package com.myoa.my_oa.Interceptor;

import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    SysUserService sysUserService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberIdByJwtToken);
        //如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //如果没有token直接执行拦截
        if(memberIdByJwtToken==0){

            throw  new CustomerException(20000,"未登录，无法访问api");
        }
        //如果无法从token值中找到相应用户对象直接拦截
//        if(sysUserService.getById(memberIdByJwtToken)==null){
//            System.out.println(sysUserService.getById(memberIdByJwtToken));
//            throw  new CustomerException(20000,"无法从token中获取对象，无法访问api");
//        }
        return true;
    }

}
