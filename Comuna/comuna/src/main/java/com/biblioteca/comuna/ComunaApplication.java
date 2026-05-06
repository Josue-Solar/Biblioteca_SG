package com.biblioteca.comuna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ComunaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComunaApplication.class, args);
	}

}
