package com.wpy.dips;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@SpringBootApplication
@MapperScan("com.wpy.dips.dao")
@Component
public class DipsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DipsApplication.class, args);
    }



}
