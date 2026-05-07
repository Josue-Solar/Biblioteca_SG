package com.biblioteca.ejemplar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LibroDTO {
    @JsonProperty("isbn")
    private Long isbn;
    @JsonProperty("nombre")
    private String nombre;
}
