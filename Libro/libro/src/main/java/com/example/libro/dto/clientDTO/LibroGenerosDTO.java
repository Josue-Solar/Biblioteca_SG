package com.example.libro.dto.clientDTO;

import java.util.List;

import com.example.libro.dto.LibroDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroGenerosDTO {
    private LibroDTO.Response libro;
    private List<GeneroDTO> generos;
}
