package com.finalproject.ildoduk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Timer;

@SpringBootApplication
@EnableJpaAuditing
public class IldodukApplication {

    public static void main(String[] args) {
        SpringApplication.run(IldodukApplication.class, args);
    }

}
