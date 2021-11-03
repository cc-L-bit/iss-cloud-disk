package com.iss.cloud.disk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.iss.cloud.disk.dao")
public class IssCloudDiskApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(IssCloudDiskApplication.class, args);
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
