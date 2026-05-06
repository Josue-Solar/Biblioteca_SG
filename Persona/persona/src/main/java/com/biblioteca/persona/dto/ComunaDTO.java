package com.biblioteca.persona.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ComunaDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("nombre")
    private String nombre;
}
