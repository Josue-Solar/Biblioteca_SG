package com.biblioteca.ejemplar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EjemplarDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private Long libroIsbn;
        private Long edicionId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long libroIsbn;
        private String nombreLibro;
        private EdicionDTO edicion;
    }
}
