package com.example.edicion.dto.clientDTO.editorialclient;

import java.util.List;

import com.example.edicion.model.Edicion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EdicionEditorialesDTO {

    private Edicion edicion;
    private List<EditorialDTO> editorial;
}
