package com.example.edicion.dto.clientDTO.editorialclient;

import java.util.List;

import com.example.edicion.dto.EdicionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditorialEdicionesDTO {
    private EditorialDTO editorial;
    private List<EdicionDTO.Response> ediciones;
}
