package com.biblioteca.ejemplar.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.ejemplar.config.FeignConfig;
import com.biblioteca.ejemplar.dto.LibroDTO;

import jakarta.validation.Valid;

@FeignClient(name = "libro", url = "http://localhost:8080", configuration = FeignConfig.class)
public interface LibroClient {
    @GetMapping("/api/v1/libros/isbn:{isbn}")
    LibroDTO getByID(@Valid @PathVariable long isbn);
}
