package com.biblioteca.autor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Autor")
                        .version("1.0")
                        .description("API REST para la gestion de autores en el sistema de la Biblioteca."));
    }

}
