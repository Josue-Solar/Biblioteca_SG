package com.example.libro.dto.clientDTO.ejemplarClient;

import java.util.List;

import com.example.libro.model.Libro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroEjemplaresDTO {
    private Libro libro;
    private List<EjemplarDTO> ejemplares;
}
