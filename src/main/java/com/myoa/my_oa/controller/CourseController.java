package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.Course;
import com.myoa.my_oa.entity.StudentCourse;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.service.CourseService;
import com.myoa.my_oa.service.StudentCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/my_oa/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;

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
        courseLambdaQueryWrapper.last("limit 6");
        List<Course> list = courseService.list(courseLambdaQueryWrapper);
        return R.ok().data("coursedata",list);
    }
}

