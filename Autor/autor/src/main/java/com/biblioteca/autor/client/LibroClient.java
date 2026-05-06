package com.biblioteca.autor.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.autor.config.FeignConfig;
import com.biblioteca.autor.dto.LibroDTO;

@FeignClient(name = "libro", url = "http://localhost:8080", configuration = FeignConfig.class)
public interface LibroClient {

    @GetMapping("/api/v1/libros/autorId/{id}")
    public List<LibroDTO> getAllByAuthId(@PathVariable Long id);
}
