package com.biblioteca.persona.service.impl;

import com.biblioteca.persona.repository.RolRepository;
import com.biblioteca.persona.repository.SexoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.biblioteca.persona.client.ComunaClient;
import com.biblioteca.persona.dto.ComunaDTO;
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.repository.PersonaRepository;
import com.biblioteca.persona.service.PersonaService;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.SexoService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonaServiceImpl implements PersonaService{

    private final RolService rolService;
    private final SexoService sexoService;
    private final RolRepository rolRepository;
    private final SexoRepository sexoRepository;
    private final PersonaRepository personaRepository;
    private final ComunaClient comunaClient;

    //ver todas las personas
    public List<PersonaDTO.Response> findAll(){
        return personaRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // buscar por id con excepcion
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
            persona.setRol(rolRepository.findById(request.getIdRol()).orElseThrow(() -> new RuntimeException("Rol no encontrado")));
            persona.setComunaId(request.getComunaId());

            Persona guardada = personaRepository.save(persona);
            return mapToResponse(guardada);
        }
    }

    //updatear por run
    public PersonaDTO.Response updatePersona(Long id, PersonaDTO.Request request){
        // 1. Buscamos a la persona por su ID inmutable
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una persona con el ID: " + id));

        // 2. Validación de seguridad: Si están cambiando el RUN, verificamos que el nuevo no exista ya
        if (!persona.getRun().equals(request.getRun()) && existsByRun(request.getRun())) {
            throw new RuntimeException("El nuevo RUT ya está registrado en otra cuenta: " + request.getRun());
        }

        // 3. Actualizamos los campos
        persona.setRun(request.getRun());
        persona.setDvRun(request.getDvRun());
        persona.setPNombre(request.getPNombre());
        persona.setSNombre(request.getSNombre());
        persona.setApPaterno(request.getApPaterno());
        persona.setApMaterno(request.getApMaterno());
        persona.setDireccion(request.getDireccion());
        persona.setCorreo(request.getCorreo());
        
        persona.setSexo(sexoRepository.findById(request.getSexoId())
                .orElseThrow(() -> new RuntimeException("Sexo no encontrado")));
                
        persona.setRol(rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado")));
                
        persona.setComunaId(request.getComunaId());

        // 4. Guardamos y mapeamos la respuesta
        Persona guardada = personaRepository.save(persona);
        return mapToResponse(guardada);
    
    }

    //borrar
    public void delete(Long id){
        personaRepository.deleteById(id);
    }

    //buscar por run
    public PersonaDTO.Response findByRun(String run) {
        Persona persona = personaRepository.findByRun(run).orElse(null);//.orElseThrow(() -> new RuntimeException());
        if (persona == null) {  //agregado el if para q de 404
        return null;
        }
        return mapToResponse(persona);
    }

    // Verificar si existe por RUN
    public boolean existsByRun(String run){
        return personaRepository.existsByRun(run);
    }

    //buscar por apellido
    public List<PersonaDTO.Response> findByApPaterno(String apPaterno){
        List<Persona> personas = personaRepository.findByApPaterno(apPaterno);
        List<PersonaDTO.Response> personasResponse = new ArrayList<>();

        /*for (Persona per : personas) {
            personasResponse.add(mapToResponse(per));
        }*/

        personas.forEach(per -> personasResponse.add(mapToResponse(per)));

        return personasResponse;
    }

    //buscar por rol
    public List<PersonaDTO.Response> findByRol(RolDTO.Response rol){
        List<Persona> personas = personaRepository.findByRolNombre(rol.getNombre());
        List<PersonaDTO.Response> personasResponse = new ArrayList<>();
        
        personas.forEach(per -> personasResponse.add(mapToResponse(per)));

        return personasResponse;
    }

    // Buscar personas por sexo
    public List<PersonaDTO.Response> findBySexo(SexoDTO.Response sexo) {
        List<Persona> personas = personaRepository.findBySexoNombre(sexo.getNombre());
        List<PersonaDTO.Response> personasResponse = new ArrayList<>();
        
        personas.forEach(per -> personasResponse.add(mapToResponse(per)));

        return personasResponse;
    }   

    // buscar por nombre de comuna
    public List<PersonaDTO.Response> findByComunaNombre(String nombreComuna){
        ComunaDTO comuna = comunaClient.buscarPorNombre(nombreComuna);
        List<Persona> personas = personaRepository.findByComunaId(comuna.getId());
        // 3. Convertimos a la lista estándar
        return personas.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Buscar por ID de comuna
    public List<PersonaDTO.Response> findByComunaID(Long id) {
        // Aquí es más fácil, buscamos directo por el ID
        List<Persona> personas = personaRepository.findByComunaId(id);
        
        return personas.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PersonaDTO.Response mapToResponse(Persona persona){
        return new PersonaDTO.Response(
            persona.getId(),
            persona.getNombreYApellido(), 
            persona.getRut(), 
            persona.getCorreo(), 
            comunaClient.buscarPorId(persona.getComunaId()), 
            sexoService.mapToResponse(persona.getSexo()), 
            rolService.mapToResponse(persona.getRol())
        );

    }

}
