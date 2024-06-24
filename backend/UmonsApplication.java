package com.genieLogiciel.Umons.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UmonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmonsApplication.class, args);
	}

}
