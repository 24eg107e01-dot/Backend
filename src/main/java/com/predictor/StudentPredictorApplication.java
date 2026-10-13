package com.predictor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentPredictorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPredictorApplication.class, args);
        System.out.println("=================================================================");
        System.out.println(" Intelligent Student Performance & Career Path Predictor Backend ");
        System.out.println(" Status: ONLINE & READY");
        System.out.println(" Docs/Health: http://localhost:8080/api/health");
        System.out.println("=================================================================");
    }
}
