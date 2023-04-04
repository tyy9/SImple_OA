package com.myoa.my_oa.service.impl;

import com.aliyun.oss.OSSClient;
import com.myoa.my_oa.service.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@Service
public class OssServiceImpl implements OssService {
    @Value("${aliyun.oss.file.endpoint}")
    String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    String keyid;
    @Value("${aliyun.oss.file.keysecret}")
    String keysecret;
    @Value("${aliyun.oss.file.bucketname}")
    String bucketname;
    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
        //创建实例
        OSSClient ossClient = new OSSClient(endpoint,keyid,keysecret);
        //获取文件输出流
        InputStream inputStream = multipartFile.getInputStream();
        //获取文件原来名字，对文件名进行加密
        String filename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        filename=uuid+filename;
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String datetime = dateTimeFormatter.format(LocalDateTime.now());
        System.out.println("datetime=>"+datetime);
        filename=datetime+"/"+filename;
        //上传文件到OSS
        ossClient.putObject(bucketname,filename,inputStream);
        ossClient.shutdown();
        //回显图片
        String url="https://"+bucketname+"."+endpoint+"/"+filename;
        return url;
    }

    @Override
    public boolean delete(String url) {
        OSSClient ossClient = new OSSClient(endpoint,keyid,keysecret);
        String host="https://"+bucketname+"."+endpoint+"/";
        String filename=url.substring(host.length());
        //判断是否删除成功
        boolean isdeleted=false;
        ossClient.deleteObject(bucketname,filename);
        ossClient.shutdown();
        return true;
    }

}
