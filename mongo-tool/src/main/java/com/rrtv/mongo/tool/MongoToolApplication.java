package com.rrtv.mongo.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MongoToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoToolApplication.class, args);
    }

}
