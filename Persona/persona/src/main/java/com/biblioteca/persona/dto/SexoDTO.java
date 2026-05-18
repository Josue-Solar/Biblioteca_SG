package com.biblioteca.persona.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SexoDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @NotBlank(message = "Nombre no puede estar en blanco")
        private String nombre;
    }

    @Data 
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String nombre;
    }
}
