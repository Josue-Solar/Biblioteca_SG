package com.biblioteca.reserva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.reserva.dto.PersonaDTO;


@FeignClient(name = "personas", url = "${ms.personas.url}", configuration = FeignClientConfig.class)
public interface PersonaClient {

    // Las anotaciones son EXACTAMENTE iguales a las del
    // EspecialidadController en ms-especialidades.
    // Feign las usa como "contrato" para construir la petición.
    @GetMapping("/api/v1/personas/id:{id}")       // ("/api/v1/personas") ("/id:{id}")
    PersonaDTO obtenerPorId(@PathVariable Long id);

}
