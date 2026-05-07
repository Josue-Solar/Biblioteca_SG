package com.biblioteca.reserva.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodasReservasCompletas {
    private List<ReservaCompletaDTO> reservas;
}
