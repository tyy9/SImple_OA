package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.SysFile;
import com.myoa.my_oa.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-04-04
 */
@Api(tags = "文件管理接口")
@CrossOrigin
@RestController
@RequestMapping("/my_oa/sys-file")
public class SysFileController {
    @Autowired
    SysFileService sysFileService;
    @ApiOperation(value = "文件分页接口")
    @PostMapping("/filepage/{page}/{limit}")
    public R filepage(
            @ApiParam(name="page",value = "当前页数")
            @PathVariable int page,
            @ApiParam(name="limit",value = "最大显示数")
            @PathVariable int limit,
            @ApiParam(name="file",value = "文件查找对象")
            @RequestBody(required = false)SysFile sysFile
            ){
        Page<SysFile> sysFilePage = new Page<>(page, limit);
        LambdaQueryWrapper<SysFile> sysFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysFileLambdaQueryWrapper.like(!StringUtils.isEmpty(sysFile.getName()),SysFile::getName,sysFile.getName());
        sysFileService.page(sysFilePage,sysFileLambdaQueryWrapper);
        List<SysFile> records = sysFilePage.getRecords();
        System.out.println(records);
        long total = sysFilePage.getTotal();
        return R.ok().data("data",records).data("total",total);
    }

    //删除只是逻辑删除，不删除oss里的文件
    @ApiOperation(value = "文件删除接口")
    @DeleteMapping("/{id}")
    public R deletefile(
            @ApiParam(name="id",value = "文件id")
            @PathVariable Integer id
    ){
        boolean b = sysFileService.removeById(id);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "文件批量删除接口")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name = "ids",value = "文件id集合")
            @RequestBody List<Integer> ids
    ){
        boolean b = sysFileService.removeByIds(ids);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "文件添加接口")
    @PostMapping("/addFile")
    public R addFile(
            @ApiParam(name="file",value = "文件对象")
            @RequestBody SysFile sysFile
    ){
        boolean save = sysFileService.save(sysFile);
        return save?R.ok():R.error();
    }
}

