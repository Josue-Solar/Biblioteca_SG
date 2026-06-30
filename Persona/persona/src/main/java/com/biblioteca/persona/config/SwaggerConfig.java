package com.biblioteca.persona.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI custOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Personas")
                        .version("1.0")
                        .description("API REST para la gestion de personas en el sistema de la Biblioteca."));
    }

}
