package com.example.nntask;

import org.springframework.boot.SpringApplication;

public class TestNntaskApplication {

	public static void main(String[] args) {
		SpringApplication.from(NntaskApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
