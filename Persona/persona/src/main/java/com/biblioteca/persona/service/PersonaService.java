package com.biblioteca.persona.service;

import java.util.List;


import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.dto.SexoDTO;

public interface PersonaService {
    List<PersonaDTO.Response> findAll();  
    PersonaDTO.Response findById(Long id);
    PersonaDTO.Response save(PersonaDTO.Request persona);
    PersonaDTO.Response updatePersona(Long id, PersonaDTO.Request nPersona);
    void delete(Long id);
    PersonaDTO.Response findByRun(String run);
    List<PersonaDTO.Response> findByApPaterno(String apPaterno);
    List<PersonaDTO.Response> findByRol(RolDTO.Response rol);
    List<PersonaDTO.Response> findBySexo(SexoDTO.Response sexo);
    List<PersonaDTO.Response> findByComunaNombre(String nombreComuna);
    List<PersonaDTO.Response> findByComunaID(Long id);
}
