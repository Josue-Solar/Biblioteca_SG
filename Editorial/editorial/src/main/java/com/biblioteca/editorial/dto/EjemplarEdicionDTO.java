package com.biblioteca.editorial.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EjemplarEdicionDTO {
    private EdicionDTO edicion;
    private List<EjemplarDTO> ejemplares;
}
