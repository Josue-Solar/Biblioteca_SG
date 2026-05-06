package com.biblioteca.autor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutorApplication.class, args);
	}

}
