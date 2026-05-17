package com.example.libro.dto.clientDTO.generoClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeneroDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("nombre")
    private String nombre;
}
