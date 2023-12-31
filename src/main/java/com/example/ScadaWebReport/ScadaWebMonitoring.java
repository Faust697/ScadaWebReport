package com.example.ScadaWebReport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.example.ScadaWebReport")
@EntityScan("com.example.ScadaWebReport")
public class ScadaWebMonitoring {
    public static void main(String[] args) {
        SpringApplication.run(ScadaWebMonitoring.class, args);
    }
}
