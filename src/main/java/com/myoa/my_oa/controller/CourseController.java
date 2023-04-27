package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.Course;
import com.myoa.my_oa.entity.StudentCourse;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.service.CourseService;
import com.myoa.my_oa.service.StudentCourseService;
import com.myoa.my_oa.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-04-18
 */
@Api(tags = "课程接口")
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/my_oa/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(CourseController.class);
    @ApiOperation(value = "课程分页查询")
    @PostMapping("/pageCourse/{page}/{limit}")

    public R pageUser(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @ApiParam(name = "course",value = "课程对象")
            @RequestBody(required = false) Course course
    ){
        Page<Course> coursePage = new Page<>(page,limit);
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.like(!StringUtils.isEmpty(course.getName()),Course::getName,course.getName())
                .eq(!StringUtils.isEmpty(course.getSubjectParentid()),Course::getSubjectParentid,course.getSubjectParentid())
                .orderByDesc(Course::getId);
        courseService.page(coursePage,courseLambdaQueryWrapper);
        long total = coursePage.getTotal();
        List<Course> records = coursePage.getRecords();
        return R.ok().data("total",total).data("data",records);
    }

    @ApiOperation(value = "新增课程")
    @PostMapping("/addCourse")
    public R addCourse(
            @ApiParam(name = "course",value = "课程对象")
            @RequestBody Course course
    ){
        boolean save = courseService.save(course);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据课程id回显")
    @PostMapping("/findCourseById/{id}")
    public R findCourseById(
            @ApiParam(name = "id",value = "课程id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.eq(Course::getId,id);
        Course one = courseService.getOne(courseLambdaQueryWrapper);
        return R.ok().data("course",one);
    }

    @ApiOperation(value = "更新课程信息")
    @PostMapping("/updateCourse")
    public R updateCourse(
            @ApiParam(name = "course",value = "课程对象")
            @RequestBody  Course  course
    ){
        //先将门户的课程redis缓存删除
        redisTemplate.delete("index_course::coursedata");
        boolean b = courseService.updateById(course);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "删除课程信息")
    @DeleteMapping("/deleteCourse/{id}")
    public R deleteCourse(
            @ApiParam(name = "id",value = "课程id")
            @PathVariable  Integer id
    ){
        boolean b = courseService.removeById(id);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "批量删除课程信息")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name="ids",value = "课程id集合")
            @RequestBody List<Integer> ids
    ){
        boolean b = courseService.removeByIds(ids);
        return b?R.ok():R.error();
    }


    @ApiOperation(value = "首页课程显示数据")
    @GetMapping("/getIndexCourseData")
    @Cacheable(value = "index_course",key = "'coursedata'")
    public R getIndexCourseData(){
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //先将门户的课程redis缓存删除

        courseLambdaQueryWrapper.last("limit 6");
        List<Course> list = courseService.list(courseLambdaQueryWrapper);
        return R.ok().data("coursedata",list);
    }

    @ApiOperation(value = "根据教师id获取课程")
        @PostMapping("/getCourseByUserId/{id}")
    public R getCourseByUserId(
            @ApiParam(value = "id",name="用户id")
            @PathVariable Integer id
    ){


        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.eq(Course::getTeacherId,id);
        List<Course> list = courseService.list(courseLambdaQueryWrapper);
        return R.ok().data("course",list);
    }



}

