package com.example.edicion.client;

import feign.auth.BasicAuthRequestInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${edicion.username}")
    private String edicionUser;

    @Value("${edicion.password}")
    private String edicionPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(edicionUser, edicionPassword);
    }
}
