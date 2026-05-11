package com.example.edicion.dto;

import java.util.List;

import com.example.edicion.model.Edicion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EjemplarEdicionDTO {
    private Edicion edicion;
    private List<EjemplarDTO> ejemplares;
}
