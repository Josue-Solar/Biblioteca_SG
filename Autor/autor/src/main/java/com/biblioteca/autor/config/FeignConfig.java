package com.biblioteca.autor.config;

import feign.auth.BasicAuthRequestInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${libro.user}")
    private String libroUser;
    @Value("${libro.password}")
    private String libroPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(libroUser, libroPassword);
    }
}
