package com.myoa.my_oa.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OssService {
        String upload(MultipartFile multipartFile) throws IOException;
}
