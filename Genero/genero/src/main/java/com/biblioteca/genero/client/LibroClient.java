package com.biblioteca.genero.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.genero.config.FeignConfig;
import com.biblioteca.genero.dto.LibroDTO;

@FeignClient(name = "libro", url = "${libro.url}", configuration = FeignConfig.class)
public interface LibroClient {

    @GetMapping("/api/v1/libroGeneros/porGenero/{generoId}")
    List<LibroDTO> getAllByGeneroId(@PathVariable("generoId") long generoId);
}
