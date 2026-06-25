package com.example.edicion.dto.clientDTO.editorialclient;

import java.util.List;
import java.util.stream.Stream;

import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.dto.EdicionDTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditorialEdicionesDTO {
    private EditorialDTO editorial;
    private List<EdicionDTO.Response> ediciones;
    public Stream<Response> stream() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stream'");
    }
}
