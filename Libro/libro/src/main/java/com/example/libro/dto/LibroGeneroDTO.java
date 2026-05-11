package com.example.libro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LibroGeneroDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        
        private Long generoId;
        private Long libroIsbn; 
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long generoId;
        private Long libroIsbn;
    }
}
