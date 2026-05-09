package com.biblioteca.prestamo.client;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración del FeignClient.
 *
 * BasicAuthRequestInterceptor agrega automáticamente el header:
 *   Authorization: Basic <base64(user:password)>
 * en cada petición que hace Feign hacia ms-genero.
 *
 * Los valores se leen desde application.properties para no hardcodear credenciales.
 */

@Configuration
public class FeignClientConfig {

    // Usamos nombres genéricos porque sirven para cualquier microservicio
    @Value("${ms.seguridad.user}")
    private String apiUser;

    @Value("${ms.seguridad.password}")
    private String apiPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(apiUser, apiPassword);
    }
}
