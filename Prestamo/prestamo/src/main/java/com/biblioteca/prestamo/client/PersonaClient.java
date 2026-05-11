package com.biblioteca.prestamo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.prestamo.dto.PersonaDTO;

@FeignClient(name = "personas", url = "${ms.personas.url}", configuration = FeignClientConfig.class)
public interface PersonaClient {

    @GetMapping("/api/v1/personas/id:{id}")       
    PersonaDTO obtenerPorId(@PathVariable Long id);

}
