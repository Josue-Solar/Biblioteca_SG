package com.example.libro.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.libro.config.FeignConfig;
import com.example.libro.dto.clientDTO.AutorDTO;

@FeignClient(name = "autor", url = "http://localhost:8089", configuration = FeignConfig.class)
public interface AutorClient {
    @GetMapping("/api/v1/autores/id/{id}")
    AutorDTO buscarPorId(@PathVariable("id") Long id);
}
