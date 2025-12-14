package com.ssafy.gt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@MapperScan("com.ssafy.gt.mapper")
public class GtApplication {

    public static void main(String[] args) {
        SpringApplication.run(GtApplication.class, args); //test
    }

}
