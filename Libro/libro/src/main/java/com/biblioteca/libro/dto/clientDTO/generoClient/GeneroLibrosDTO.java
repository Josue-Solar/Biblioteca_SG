package com.biblioteca.libro.dto.clientDTO.generoClient;

import java.util.List;

import com.biblioteca.libro.dto.LibroDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneroLibrosDTO {
    private GeneroDTO genero;
    private List<LibroDTO.Response> libros;
}
