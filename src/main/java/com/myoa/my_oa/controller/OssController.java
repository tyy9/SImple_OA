package com.myoa.my_oa.controller;

import com.myoa.my_oa.common.R;
import com.myoa.my_oa.config.JwtUtils;
import com.myoa.my_oa.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
        @PostMapping("/upload")
        @ApiOperation(value = "OSS上传接口")
        public R upload(
                @ApiParam(name = "file",value = "上传文件")
                @RequestParam("file")MultipartFile file
                ) throws IOException {
            long size = file.getSize();

            String upload = ossService.upload(file);
            return R.ok().data("url",upload).data("size",size);
        }

        @ApiOperation(value = "OSS删除文件接口")
            @PostMapping("/delete/{url}")
        public R delete(
                @ApiParam(name="url",value = "文件路径")
                @PathVariable String url
        ){
//            System.out.println("1");
//            boolean delete = ossService.delete(url);
            return R.ok();
        }
}
