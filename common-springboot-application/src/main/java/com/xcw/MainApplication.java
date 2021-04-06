package com.xcw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @class: MainApplication
 * @author: ChengweiXing
 * @description: TODO
 **/
@SpringBootApplication
@MapperScan("com.xcw.mapper")
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
}
