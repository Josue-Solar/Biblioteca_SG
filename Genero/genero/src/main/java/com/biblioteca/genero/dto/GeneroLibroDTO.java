package com.biblioteca.genero.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneroLibroDTO {
    private GeneroDTO.Response genero;
    private List<LibroDTO> libros;
}
