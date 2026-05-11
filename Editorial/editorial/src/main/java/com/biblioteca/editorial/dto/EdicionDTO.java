package com.biblioteca.editorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EdicionDTO {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("annioPublicacion")
    private int annioPublicacion;

}
