package com.biblioteca.editorial.client;

import feign.auth.BasicAuthRequestInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${editorial.username}")
    private String editorialUser;

    @Value("${editorial.password}")
    private String editorialPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(editorialUser, editorialPassword);
    }
}
