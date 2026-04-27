package com.example.reserva.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Value("${biblioteca.ejemplar.url}")
    private String ejemplarURL;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(ejemplarURL)
                .build();
    }
}
