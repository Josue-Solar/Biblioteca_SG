package com.biblioteca.autor.dto;

import java.util.List;
import java.util.Optional;

import com.biblioteca.autor.model.Autor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroAutorDTO {
    private Autor autor;
    private List<LibroDTO> libros;
}
