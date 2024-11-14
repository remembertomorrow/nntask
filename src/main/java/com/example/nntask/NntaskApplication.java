package com.example.nntask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NntaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(NntaskApplication.class, args);
	}

}
