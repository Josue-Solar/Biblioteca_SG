package com.biblioteca.ejemplar.config;

import feign.auth.BasicAuthRequestInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${ejemplar.username}")
    private String ejemplarUser;

    @Value("${ejemplar.password}")
    private String ejemplarPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(ejemplarUser, ejemplarPassword);
    }
}
