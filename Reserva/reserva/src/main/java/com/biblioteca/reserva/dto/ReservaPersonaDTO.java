package com.biblioteca.reserva.dto;

import java.util.List;

import com.biblioteca.reserva.model.Reserva;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservaPersonaDTO {
    private PersonaDTO persona;
    private List<Reserva> reserva;
}
