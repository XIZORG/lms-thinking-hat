package com.lms.hat.thinking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication
public class ThinkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThinkingApplication.class, args);
    }
}
