package com.biblioteca.persona.service.impl;

import com.biblioteca.persona.repository.RolRepository;
import com.biblioteca.persona.repository.SexoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.client.ComunaClient;
import com.biblioteca.persona.dto.ComunaDTO;
import com.biblioteca.persona.dto.PersonaComunaDTO;
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.repository.PersonaRepository;
import com.biblioteca.persona.service.PersonaService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService{

    private final RolRepository rolRepository;
    private final SexoRepository sexoRepository;
    private final PersonaRepository personaRepository;
    private final ComunaClient comunaClient;

    //ver todas las personas
    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO.Response> findAll(){
        return personaRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // buscar por id con excepcion
    @Override
    @Transactional(readOnly = true)
    public PersonaDTO.Response findById(Long id){
        Persona persona = personaRepository.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));

        return mapToResponse(persona);
    }

    //crear
    public PersonaDTO.Response save(PersonaDTO.Request request){
        if(existsByRun(request.getRun())){
            throw new RuntimeException("Ya existe una persona con el RUT: " + request.getRun());
        }else{
            Persona persona = new Persona();
            persona.setRun(request.getRun());
            persona.setDvRun(request.getDvRun());
            persona.setPNombre(request.getPNombre());
            persona.setSNombre(request.getSNombre());
            persona.setApPaterno(request.getApPaterno());
            persona.setApMaterno(request.getApMaterno());
            persona.setDireccion(request.getDireccion());
            persona.setCorreo(request.getCorreo());
            persona.setSexo(sexoRepository.findById(request.getSexoId()).orElseThrow(() -> new RuntimeException("Sexo no encontrado")));
            persona.setRol(rolRepository.findById(request.getIdRol()).orElseThrow(() -> new RuntimeException("Sexo no encontrado")));
            persona.setComunaId(request.getComunaId());

            Persona guardada = personaRepository.save(persona);
            return mapToResponse(guardada);
        }
    }

    //updatear por run
    public PersonaDTO.Response updatePersona(String run, PersonaDTO.Request request){
        if(existsByRun(request.getRun())){
            Persona persona = new Persona();
            persona.setRun(request.getRun());
            persona.setDvRun(request.getDvRun());
            persona.setPNombre(request.getPNombre());
            persona.setSNombre(request.getSNombre());
            persona.setApPaterno(request.getApPaterno());
            persona.setApMaterno(request.getApMaterno());
            persona.setDireccion(request.getDireccion());
            persona.setCorreo(request.getCorreo());
            persona.setSexo(sexoRepository.findById(request.getSexoId()).orElseThrow(() -> new RuntimeException("Sexo no encontrado")));
            persona.setRol(rolRepository.findById(request.getIdRol()).orElseThrow(() -> new RuntimeException("Sexo no encontrado")));
            persona.setComunaId(request.getComunaId());

            Persona guardada = personaRepository.save(persona);
            return mapToResponse(guardada);
            
        }else{
            throw new RuntimeException("No existe una persona con el RUT: " + request.getRun());
        }
    }

    //borrar
    public void delete(Long id){
        personaRepository.deleteById(id);
    }

    //buscar por run
    @Override
    @Transactional(readOnly = true)
    public PersonaDTO.Response findByRun(String run) {
        Persona persona = personaRepository.findByRun(run).orElseThrow(() -> new RuntimeException());
        return mapToResponse(persona);
    }

    // Verificar si existe por RUN
    public boolean existsByRun(String run){
        return personaRepository.existsByRun(run);
    }

    //buscar por apellido
    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO.Response> findByApPaterno(String apPaterno){
        List<Persona> personas = personaRepository.findByApPaterno(apPaterno);
        List<PersonaDTO.Response> personasResponse = new ArrayList<>();

        for (Persona per : personas) {
            personasResponse.add(mapToResponse(per));
        }

        return personasResponse;
    }

    //buscar por rol
    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO.Response> findByRol(Rol rol){
        List<Persona> personas = personaRepository.findByRol(rol);
        List<PersonaDTO.Response> personasResponse = new ArrayList<>();

        for (Persona per : personas) {
            personasResponse.add(mapToResponse(per));
        }

        return personasResponse;
    }

    // Buscar personas por sexo
    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO.Response> findBySexo(Sexo sexo) {
        List<Persona> personas = personaRepository.findBySexo(sexo);
        List<PersonaDTO.Response> personasResponse = new ArrayList<>();

        for (Persona per : personas) {
            personasResponse.add(mapToResponse(per));
        }

        return personasResponse;
    }   

    @Override
    @Transactional(readOnly = true)
    public PersonaComunaDTO findByComunaNombre(String nombreComuna){
        ComunaDTO comuna = comunaClient.buscarPorNombre(nombreComuna);
        PersonaComunaDTO persoComuDTO = new PersonaComunaDTO(comuna, personaRepository.findByComunaId(comuna.getId()));
        return persoComuDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaComunaDTO findByComunaID(Long id){
        ComunaDTO comuna = comunaClient.buscarPorId(id);
        PersonaComunaDTO persoComuDTO = new PersonaComunaDTO(comuna, personaRepository.findByComunaId(id));
        return persoComuDTO;
    }

    public PersonaDTO.Response mapToResponse(Persona persona){
        return new PersonaDTO.Response(
            persona.getNombreYApellido(), 
            persona.getRut(), 
            persona.getCorreo(), 
            comunaClient.buscarPorId(persona.getComunaId()), 
            persona.getSexo(), 
            persona.getRol());

    }

}
