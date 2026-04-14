package com.smartstudyplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SmartStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartStudyApplication.class, args);
    }
}