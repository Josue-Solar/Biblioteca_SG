package com.biblioteca.reserva.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReservaDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "El id de la persona es obligatorio")
        @Positive(message = "El ID de la persona debe ser un numero positivo")
        private Long personaId;

        @NotNull(message = "El id del ejemplar es obligatorio")
        @Positive(message = "El ID del ejemplar debe ser un numero positivo")
        private Long ejemplarId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private LocalDate fechaReserva;
        private LocalDate fechaExpiracion;
        private String estado; // Lo devolvemos como String para que en el JSON se vea asi (ej: "ACTIVA")
        private LocalDate fechaRetiro; // Será null al principio, se llenará en el futuro

        // FKs
        private PersonaDTO persona;   
        private EjemplarDTO ejemplar;
        public Long getPersonaId() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getPersonaId'");
        }
        public Long getEjemplarId() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getEjemplarId'");
        }

    }    
}
