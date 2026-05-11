package com.example.libro.dto.clientDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeneroDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("nombre")
    private String nombre;
}
