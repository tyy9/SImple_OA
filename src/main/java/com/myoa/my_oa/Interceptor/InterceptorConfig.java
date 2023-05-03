package com.myoa.my_oa.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public JwtInterceptor jwtInterceptor(){return  new JwtInterceptor();}
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")//拦截所有api请求,由自定义的拦截器来决定是否通行
                .excludePathPatterns("/my_oa/sys-user/login")
                .excludePathPatterns("/my_oa/sys-user/register")
                .excludePathPatterns("/my_oa/sys-user/register_common")
                .excludePathPatterns("/my_oa/sys-user/getToken")
                .excludePathPatterns("/my_oa/sys-user/getIndexTeacher")
                .excludePathPatterns("/my_oa/sys-user/getTeacherByCourseId/**")
                .excludePathPatterns("/my_oa/sys-user/findUserById/**")
                .excludePathPatterns("/my_oa/sys-user/pageUser/**")
                .excludePathPatterns("/my_oa/course/getIndexCourseData")
                .excludePathPatterns("/my_oa/course/findCourseById/**")
                .excludePathPatterns("/my_oa/course/getCourseByUserId/**")
                .excludePathPatterns("/my_oa/course/pageCourse/**")
                .excludePathPatterns("/my_oa/subject/findSubjectById/**")
                .excludePathPatterns("/my_oa/subject/getAllSubject/**")
                .excludePathPatterns("/my_oa/comment/pageComment_Course/**")
                .excludePathPatterns("/my_oa/comment/pageTeacher_Course/**")
                .excludePathPatterns("/my_oa/course-order/PageUserOrder/**")
                .excludePathPatterns("/my_oa/index/**")
                .excludePathPatterns("/my_oa/sys-role/getAllRole")
                .excludePathPatterns("/doc.html",
                        "/webjars/**",
                        "/swagger-resources",
                        "/v2/api-docs");
//                .excludePathPatterns("/my_oa/oss/upload","/my_oa/oss/delete");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置拦截器访问静态资源
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
