package com.biblioteca.reserva.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EjemplarDTO {
    @JsonProperty("isbn")
    private Long libroIsbn;

    @JsonProperty("nombre")
    private String nombre;
}
