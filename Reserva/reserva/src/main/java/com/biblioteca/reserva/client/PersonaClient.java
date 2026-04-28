package com.example.reserva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Persona", url = "http://localhost:8085")
public interface PersonaClient {
    @GetMapping("/api/personas/id/{id}")
    String buscarPorId(@PathVariable long id);
}
