package com.example.libro.dto.clientDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AutorDTO {
    @JsonProperty("id")
    private Long autorId;

    @JsonProperty("pNombre")
    private String pNombre;

    @JsonProperty("sNombre")
    private String sNombre;

    @JsonProperty("apPaterno")
    private String aPaterno;

    @JsonProperty("apMaterno")
    private String aMaterno;
}
