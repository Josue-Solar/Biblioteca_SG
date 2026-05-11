package com.example.libro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LibroDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        
        private Long isbn;
        
        @NotBlank(message = "El nombre no puede estar vacío")
        private String nombre;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long isbn;
        private String nombre;
    }
}
