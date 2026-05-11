package com.example.libro.dto.clientDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EjemplarDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("libroIsbn")
    private Long libroIsbn;
}
