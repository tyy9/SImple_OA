package com.myoa.my_oa.controller;

import com.myoa.my_oa.common.R;
import com.myoa.my_oa.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            String upload = ossService.upload(file);
            return R.ok().data("url",upload);
        }
}
