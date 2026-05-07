package com.biblioteca.genero.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LibroDTO {
    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("nombre")
    private String nombre;
}
