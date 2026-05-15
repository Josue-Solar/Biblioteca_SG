package com.biblioteca.comuna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ComunaDTO {

    // LO QUE ENTRA: Datos que te manda el usuario para crear una Comuna
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre de la comuna es obligatorio")
        private String nombre;
    }

    // LO QUE SALE: Lo que le devuelves a la pantalla o a otro microservicio
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nombre;
    }

}
