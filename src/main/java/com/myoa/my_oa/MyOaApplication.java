package com.myoa.my_oa;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.myoa.my_oa.mapper")
@SpringBootApplication
public class MyOaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyOaApplication.class, args);
    }

}
