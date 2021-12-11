package com.cheng.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODU: 2.token - redis 3.其他接口 查询nav 查询当前登录人
 */
@SpringBootApplication
@MapperScan("com.cheng.manage.mapper")
public class ManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }

}
