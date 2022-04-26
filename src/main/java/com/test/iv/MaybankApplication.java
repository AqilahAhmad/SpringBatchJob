package com.test.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableSpringConfigured
public class MaybankApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaybankApplication.class, args);
	}

}
