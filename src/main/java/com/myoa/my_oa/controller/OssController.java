package com.myoa.my_oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.entity.SysFile;
import com.myoa.my_oa.service.OssService;
import com.myoa.my_oa.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "OSS接口")
@CrossOrigin
@RestController
@RequestMapping("/my_oa/oss")
public class OssController {
            @Autowired
    OssService ossService;
            @Autowired
    SysFileService sysFileService;
        @PostMapping("/upload")
        @ApiOperation(value = "OSS上传接口")
        public R upload(
                @ApiParam(name = "file",value = "上传文件")
                @RequestParam("file")MultipartFile file
                ) throws IOException {
            long size = file.getSize();
            String filename = file.getOriginalFilename();
            String type = filename.substring(filename.lastIndexOf(".")+1);
            System.out.println(type);
            String upload = ossService.upload(file);
            //同时添加到文件管理
            SysFile sysFile = new SysFile();
            sysFile.setName(filename);
            sysFile.setType(type);
            sysFile.setSize(size);
            sysFile.setUrl(upload);
            sysFileService.save(sysFile);
            return R.ok().data("url",upload).data("size",size).data("type",type);
        }

        @ApiOperation(value = "OSS删除文件接口")
            @PostMapping("/delete")
        public R delete(
                @ApiParam(name="file",value = "文件对象")
                @RequestBody SysFile sysFile
                ){
            System.out.println("1");
            LambdaQueryWrapper<SysFile> sysFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysFileLambdaQueryWrapper.eq(!StringUtils.isEmpty(sysFile.getUrl()),SysFile::getUrl,sysFile.getUrl());
            SysFile one = sysFileService.getOne(sysFileLambdaQueryWrapper);
            if(one!=null){
                sysFileService.removeById(one);
            }
            boolean delete = ossService.delete(sysFile.getUrl());
            return R.ok();
        }

}
