package com.biblioteca.libro.dto.clientDTO.autorClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AutorDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("primerNombre")
    private String primerNombre;

    @JsonProperty("apPaterno")
    private String apPaterno;

}
