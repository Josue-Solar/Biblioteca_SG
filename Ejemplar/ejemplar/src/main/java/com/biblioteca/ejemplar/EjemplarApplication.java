package com.biblioteca.ejemplar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EjemplarApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjemplarApplication.class, args);
	}

}
