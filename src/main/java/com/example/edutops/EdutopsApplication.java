package com.example.edutops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EdutopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdutopsApplication.class, args);
	}

}
