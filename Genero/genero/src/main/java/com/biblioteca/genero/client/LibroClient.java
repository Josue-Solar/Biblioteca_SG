package com.biblioteca.genero.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblioteca.genero.config.FeignConfig;
import com.biblioteca.genero.dto.LibroDTO;

@FeignClient(name = "libro", url = "${libro.url}", configuration = FeignConfig.class)
public interface LibroClient {
    @GetMapping("/api/v1/libros/nombre")
    LibroDTO buscarPorNombre(@RequestParam String nombre);

    @GetMapping("/api/v1/libros/isbn/{isbn}")
    LibroDTO buscarPorIsbn(@PathVariable("isbn") long isbn);
}
