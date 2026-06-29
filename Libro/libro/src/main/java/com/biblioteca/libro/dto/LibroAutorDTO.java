package com.biblioteca.libro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LibroAutorDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private Long autorId;
        private Long libroIsbn;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long autorId;
        private Long libroIsbn;
    }
}
