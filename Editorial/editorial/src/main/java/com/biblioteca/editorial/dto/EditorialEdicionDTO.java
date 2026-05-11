package com.biblioteca.editorial.dto;

import java.util.List;

import com.biblioteca.editorial.model.Editorial;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditorialEdicionDTO {
    private Editorial editorial;
    private List<EjemplarEdicionDTO> edicionesEjemplares;
}
