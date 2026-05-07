package com.biblioteca.reserva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.reserva.config.FeignConfig;
import com.biblioteca.reserva.dto.PersonaDTO;

@FeignClient(name = "Persona", url = "http://localhost:8085", configuration = FeignConfig.class)
public interface PersonaClient {
    @GetMapping("/api/v1/personas/id:{id}")
    PersonaDTO buscarPorId(@PathVariable long id);
}
