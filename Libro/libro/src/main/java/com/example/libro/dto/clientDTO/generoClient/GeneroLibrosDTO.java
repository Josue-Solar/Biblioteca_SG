package com.example.libro.dto.clientDTO.generoClient;

import java.util.List;

import com.example.libro.dto.LibroDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneroLibrosDTO {
    private GeneroDTO genero;
    private List<LibroDTO.Response> libros;
}
