package com.biblioteca.editorial.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.editorial.dto.EdicionDTO;
import com.biblioteca.editorial.dto.EjemplarEdicionDTO;

@FeignClient(name = "edicion", url = "${edicion.url}", configuration = FeignConfig.class)
public interface EdicionClient {
    @GetMapping("/api/v1/ediciones/{id}")
    EdicionDTO buscarPorId(@PathVariable long id);

    @GetMapping("/api/v1/ediciones/librosPorEdicion/{edicionId}")
    EjemplarEdicionDTO librosPorEdicion(@PathVariable Long edicionId);
}
