package com.example.edicion.dto.clientDTO.editorialclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EditorialDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("nombre")
    private String nombre;
}
