package com.biblioteca.autor.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AutorDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    private String primerNombre;

    @Size(max = 50)
    private String segundoNombre;

    @Size(max = 50)
    @NotBlank(message = "El primero apellido es obligatorio")
    private String apPaterno;

    @Size(max = 50)
    private String apMaterno;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String primerNombre;
        private String segundoNombre;
        private String apPaterno;
        private String apMaterno;
    }
}
