package com.myoa.my_oa.Interceptor;

import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
//        String origin = request.getHeader("Origin");
//// 响应标头指定 指定可以访问资源的URL路径
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "1800");
////  设置  受支持请求标头（自定义  可以访问的请求头  例如：Token）
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,token,Origin,Content-Type,Accept");
//// 指示的请求的响应是否可以暴露于该页面。当true值返回时它可以被暴露
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("放行options");
            return true;
        }
        String token = request.getHeader("Authorization");
        System.out.println("Authorization=>"+token);
        String token2 = request.getHeader("token");
        System.out.println("token2=>"+token2);
        //如果不是映射到方法直接通过

        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //如果没有token直接执行拦截
        if(JwtUtils.getMemberIdByJwtToken_OSS(request)==0){

            throw  new CustomerException(21,"未登录，无法访问api");
        }
        //如果无法从token值中找到相应用户对象直接拦截
//        if(sysUserService.getById(memberIdByJwtToken)==null){
//            System.out.println(sysUserService.getById(memberIdByJwtToken));
//            throw  new CustomerException(20000,"无法从token中获取对象，无法访问api");
//        }
        return true;
    }

}
