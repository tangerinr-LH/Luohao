package com.luohao.lhdemo;

import com.luohao.lhdemo.entity.User;
import com.luohao.lhdemo.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@SpringBootApplication
@MapperScan("com.luohao.lhdemo.mapper")
public class LHdemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(LHdemoApplication.class, args);
    }



}
