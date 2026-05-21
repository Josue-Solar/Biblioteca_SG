package com.biblioteca.prestamo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PrestamoDTO {

    // LO QUE ENTRA: Datos que te manda el usuario para crear
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "La fecha de inicio es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        private LocalDate fechaInicio;

        @NotNull(message = "La fecha limite es obligatoria")
        @Future(message = "La fecha limite debe ser futura")
        private LocalDate fechaFin;

        @NotNull(message = "El id de la persona es obligatorio")
        @Positive(message = "El ID debe ser positivo")
        private Long personaId;

        @NotNull(message = "El id del ejemplar es obligatorio")
        @Positive(message = "El ID debe ser positivo")
        private Long ejemplarId;
    }    
    
    // LO QUE SALE: El recibo que tú le devuelves a la pantalla
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private LocalDate fechaInicio;     //las fechas hay q ponerlas en el postman asi:
        private LocalDate fechaFin;        // "2026-05-19"
        private LocalDate fechaDevolucion; // anio-mes-dia
        
        // Aquí sí va si está atrasado, lo calcularemos en el Service
        private boolean atrasado; 

        // Los datos que traeremos con Feign
        private PersonaDTO persona; 
        private EjemplarDTO ejemplar;
    }
}
