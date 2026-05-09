package com.biblioteca.prestamo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.prestamo.dto.PersonaDTO;

// ═══════════════════════════════════════════════════
// ESTE ARCHIVO NO EXISTE EN PARTE A (WebClient).
// Es el núcleo del cambio en la Parte B.
//
// @FeignClient declara que esta interfaz es un cliente HTTP.
//   name: identificador lógico del servicio destino.
//   url:  dirección del servicio. Se lee de application.properties
//         igual que @Value pero dentro de la anotación.
//
// Spring Cloud genera automáticamente la implementación:
//   1. Recibe la llamada  obtenerPorId(id)
//   2. Construye  GET http://localhost:8081/api/especialidades/{id}
//   3. Envía la petición HTTP
//   4. Deserializa el JSON a String
//   5. Devuelve el resultado
//   6. Si 404 → lanza FeignException.NotFound
//
// Todo sin que escribamos ni una línea de código HTTP.
// ═══════════════════════════════════════════════════
@FeignClient(name = "personas", url = "${ms.personas.url}", configuration = FeignClientConfig.class)
public interface PersonaClient {

    // Las anotaciones son EXACTAMENTE iguales a las del
    // EspecialidadController en ms-especialidades.
    // Feign las usa como "contrato" para construir la petición.
    @GetMapping("/api/v1/personas/id:{id}")       // ("/api/v1/personas") ("/id:{id}")
    PersonaDTO obtenerPorId(@PathVariable Long id);

}
