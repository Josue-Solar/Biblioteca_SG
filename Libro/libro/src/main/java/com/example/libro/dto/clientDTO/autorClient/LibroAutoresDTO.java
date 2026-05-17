package com.example.libro.dto.clientDTO.autorClient;

import java.util.List;

import com.example.libro.model.Libro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroAutoresDTO {
    private Libro libro;
    private List<AutorDTO> autor;
}
