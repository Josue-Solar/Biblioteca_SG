package com.biblioteca.ejemplar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EdicionDTO {
    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("annioPublicacion")
    private int annioPublicacion;
}
