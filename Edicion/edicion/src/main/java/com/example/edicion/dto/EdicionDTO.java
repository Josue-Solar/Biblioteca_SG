package com.example.edicion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EdicionDTO {
    
    // LO QUE ENTRA: Datos que te manda el usuario
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre de la edición es obligatorio")
        private String nombre;

        // Se usa @NotNull (requiere Integer) o @Min para números, NUNCA @NotBlank
        @NotNull(message = "El año de la edición es obligatorio")
        @Min(value = 1000, message = "El año ingresado no es válido")
        private Integer annioPublicacion; 
    }

    // LO QUE SALE: Lo que se devuelve al cliente
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        
        private Long id; // Agregado: Vital para los enlaces HATEOAS y el Assembler
        private String nombre;
        private Integer annioPublicacion;
        
        // ¡Se elimina el getId() manual que lanzaba la excepción!
        // Lombok (@Data) ya se encarga de generar getId(), getNombre(), etc., automáticamente.
    }
}