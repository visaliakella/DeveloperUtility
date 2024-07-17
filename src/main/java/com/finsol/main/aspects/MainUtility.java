package com.finsol.main.aspects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class MainUtility {

    public static void main(String[] args) {
        SpringApplication.run(MainUtility.class, args);
        System.out.println("Hello and welcome!");
    }
}
