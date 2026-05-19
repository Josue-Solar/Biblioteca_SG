package com.example.edicion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EdicionDTO {
    //request
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre de la edicion es obligatorio")
        private String nombre;

        @NotBlank(message = "El año de la edicion es obligatorio")
        private int annio_publicacion;
    }

    //response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String nombre;
        private int annio_publicacion;
    }
}
