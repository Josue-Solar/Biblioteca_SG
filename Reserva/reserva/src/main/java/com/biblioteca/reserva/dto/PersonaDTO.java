package com.biblioteca.reserva.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonaDTO {

    @JsonProperty("pNombre")
    private String pNombre;

    @JsonProperty("apPaterno")
    private String apPaterno;

}
