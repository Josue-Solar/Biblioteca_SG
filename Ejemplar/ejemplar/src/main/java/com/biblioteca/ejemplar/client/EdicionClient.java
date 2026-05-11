package com.biblioteca.ejemplar.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.ejemplar.config.FeignConfig;
import com.biblioteca.ejemplar.dto.EdicionDTO;

@FeignClient(name = "edicion", url = "${edicion.url}", configuration = FeignConfig.class)
public interface EdicionClient {
    @GetMapping("/api/v1/ediciones/{id}")
    EdicionDTO buscarPorId(@PathVariable Long id);
}
