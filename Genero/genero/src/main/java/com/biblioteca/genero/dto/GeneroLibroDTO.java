package com.biblioteca.genero.dto;

import java.util.List;

import com.biblioteca.genero.model.Genero;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneroLibroDTO {
    private LibroDTO libro;
    private List<Genero> generos;
}
