package com.biblioteca.prestamo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.prestamo.dto.PersonaDTO;


// @FeignClient declara que esta interfaz es un cliente HTTP.
//   name: identificador lógico del servicio destino.
//   url:  dirección del servicio. Se lee de application.properties
//         igual que @Value pero dentro de la anotación.

@FeignClient(name = "personas", url = "${ms.personas.url}", configuration = FeignClientConfig.class)
public interface PersonaClient {
    // Las anotaciones son EXACTAMENTE iguales a las del
    // EspecialidadController en ms-especialidades.
    // Feign las usa como "contrato" para construir la petición.
    @GetMapping("/api/v1/personas/id:{id}")       // ("/api/v1/personas") ("/id:{id}")
    PersonaDTO obtenerPorId(@PathVariable Long id);

}
