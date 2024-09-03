package com.jh.EVSherpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EvSherpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvSherpaApplication.class, args);
    }

}
