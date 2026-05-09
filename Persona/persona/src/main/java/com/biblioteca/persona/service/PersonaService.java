package com.biblioteca.persona.service;

import java.util.List;

import com.biblioteca.persona.dto.PersonaComunaDTO;
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.model.Sexo;

public interface PersonaService {
    List<PersonaDTO.Response> findAll();
    PersonaDTO.Response findById(Long id);
    PersonaDTO.Response save(PersonaDTO.Request persona);
    PersonaDTO.Response updatePersona(String run, PersonaDTO.Request nPersona);
    void delete(Long id);
    PersonaDTO.Response findByRun(String run);
    List<PersonaDTO.Response> findByApPaterno(String apPaterno);
    List<PersonaDTO.Response> findByRol(Rol rol);
    List<PersonaDTO.Response> findBySexo(Sexo sexo);
    PersonaComunaDTO findByComunaNombre(String nombreComuna);
    PersonaComunaDTO findByComunaID(Long id);
}
