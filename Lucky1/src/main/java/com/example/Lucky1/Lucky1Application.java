package com.example.Lucky1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Lucky1Application {
	public static void main(String[] args) {
		SpringApplication.run(Lucky1Application.class, args);
	}
}
