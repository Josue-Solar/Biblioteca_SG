package com.biblioteca.persona.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RolDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 30)
        private String nombre;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String nombre;
    }
}
