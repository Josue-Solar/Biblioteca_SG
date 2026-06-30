package com.biblioteca.persona.dto;

import java.util.List;

import com.biblioteca.persona.model.Persona;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonaComunaDTO {
    private ComunaDTO comuna;
    private List<Persona> personas;
}
