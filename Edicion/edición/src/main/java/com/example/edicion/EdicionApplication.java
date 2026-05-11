package com.example.edicion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EdicionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdicionApplication.class, args);
	}

}
