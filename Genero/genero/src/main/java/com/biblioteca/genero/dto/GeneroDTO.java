package com.biblioteca.genero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GeneroDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        @NotBlank(message = "El nombre no puede estar vacío")
        private String nombre;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private String nombre;

        public long getId() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getId'");
        }
    }
}
