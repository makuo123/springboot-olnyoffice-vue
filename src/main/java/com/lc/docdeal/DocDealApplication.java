package com.lc.docdeal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//启动类
@SpringBootApplication
@MapperScan("com.lc.docdeal.mapper")
public class DocDealApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocDealApplication.class,args);
    }
}
