package com.biblioteca.editorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EditorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(EditorialApplication.class, args);
	}

}
