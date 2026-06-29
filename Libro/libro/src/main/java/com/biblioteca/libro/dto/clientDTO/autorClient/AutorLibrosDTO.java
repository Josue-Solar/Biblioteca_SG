package com.biblioteca.libro.dto.clientDTO.autorClient;

import java.util.List;

import com.biblioteca.libro.dto.LibroDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutorLibrosDTO {
    private AutorDTO autor;
    private List<LibroDTO.Response> libros;


}
