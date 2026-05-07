package com.biblioteca.reserva.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservaCompletaDTO {
    private Long idReserva;
    private PersonaDTO personaDTO;
    private EjemplarDTO ejemplar;
}
