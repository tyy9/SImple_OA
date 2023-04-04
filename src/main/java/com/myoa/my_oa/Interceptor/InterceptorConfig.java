package com.myoa.my_oa.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")//拦截所有api请求,由自定义的拦截器来决定是否通行
                .excludePathPatterns("/my_oa/sys-user/login")
                .excludePathPatterns("/my_oa/sys-user/register")
                .excludePathPatterns("/doc.html",
                        "/webjars/**",
                        "/swagger-resources",
                        "/v2/api-docs")
                .excludePathPatterns("/my_oa/oss/*");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置拦截器访问静态资源
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
