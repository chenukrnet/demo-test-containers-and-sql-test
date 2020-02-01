package com.example.demosqltest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoApplicationWithoutSql {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplicationWithoutSql.class, args);
    }


}
