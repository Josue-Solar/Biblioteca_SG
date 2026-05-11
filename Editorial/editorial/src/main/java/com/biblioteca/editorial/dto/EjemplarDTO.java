package com.biblioteca.editorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EjemplarDTO {
    @JsonProperty("libroIsbn")
    private Long libroIsbn;
    @JsonProperty("nombreLibro")
    private String nombreLibro;
}
