package com.biblioteca.reserva.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonaDTO {
    private long id;

    @JsonProperty("pNombre")
    private String pNombre;

    @JsonProperty("apPaterno")
    private String apPaterno;
    
    public String getNombreCompleto(){
        return pNombre + " " + apPaterno;
    }

}
