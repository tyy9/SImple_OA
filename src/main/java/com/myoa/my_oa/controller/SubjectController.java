package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.Subject;
import com.myoa.my_oa.entity.SysMenu;
import com.myoa.my_oa.entity.dto.SubjectDto;
import com.myoa.my_oa.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-04-18
 */
@Api(tags = "课程分类接口")
@CrossOrigin
@RestController
@RequestMapping("/my_oa/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;
    @ApiOperation(value = "课程所有分类获取接口")
    @GetMapping("/getAllSubject")
    public R  getAllSubject(){
        List<SubjectDto> allSubjectDto = subjectService.getAllSubjectDto();
        return R.ok().data("subjectList",allSubjectDto);
    }

    @ApiOperation(value = "课程分类添加接口")
    @PostMapping("/addSubject")
    public R addSubject(
            @ApiParam(name="subject",value = "课程分类对象")
            @RequestBody Subject subject
    ){
        boolean save = subjectService.save(subject);
        return save?R.ok():R.error();
    }

    @ApiOperation(value = "根据课程id回显")
    @PostMapping("/findSubjectById/{id}")
    public R findSubjectById(
            @ApiParam(name = "id",value = "课程分类id")
            @PathVariable String id
    ){
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.eq(Subject::getId,id);
        Subject one = subjectService.getOne(subjectLambdaQueryWrapper);
        return R.ok().data("subject",one);
    }

    @ApiOperation(value = "更新课程分类信息")
    @PostMapping("/updateSubject")
    public R updateSubject(
            @ApiParam(name = "subject",value = "课程分类对象")
            @RequestBody  Subject subject
    ){

        boolean b = subjectService.updateById(subject);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "删除菜单信息")
    @DeleteMapping("/deleteSubject/{id}")
    public R deleteSubject(
            @ApiParam(name = "id",value = "课程id")
            @PathVariable  String id
    ){
        boolean b = subjectService.removeById(id);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "批量删除菜单信息")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name="ids",value = "课程分类id集合")
            @RequestBody List<String> ids
    ){
        boolean b = subjectService.removeByIds(ids);
        return b?R.ok():R.error();
    }
}

