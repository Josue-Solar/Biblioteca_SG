package com.example.libro.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.libro.config.FeignConfig;
import com.example.libro.dto.clientDTO.generoClient.GeneroDTO;

@FeignClient(name = "genero", url = "${genero.url}", configuration = FeignConfig.class)
public interface GeneroClient {
    @GetMapping("/api/v1/generos/{id}")
    GeneroDTO buscarPorId(@PathVariable("id") long id);
}
