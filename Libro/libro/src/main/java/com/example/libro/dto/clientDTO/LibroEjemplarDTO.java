package com.example.libro.dto.clientDTO;

import java.util.List;

import com.example.libro.model.Libro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroEjemplarDTO {
    private Libro libro;
    private List<EjemplarDTO> ejemplares;
}
