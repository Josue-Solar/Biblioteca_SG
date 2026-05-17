package com.example.libro.dto.clientDTO.autorClient;

import java.util.List;

import com.example.libro.dto.LibroDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutorLibrosDTO {
    private AutorDTO autor;
    private List<LibroDTO.Response> libros;


}
