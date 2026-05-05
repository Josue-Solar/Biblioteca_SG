package com.biblioteca.persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import com.biblioteca.persona.model.Persona;
=======
import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Sexo;
>>>>>>> usuario
import com.biblioteca.persona.repository.PersonaRepository;

import jakarta.transaction.Transactional;

@Service
<<<<<<< HEAD
@Transactional
=======
@Transactional //sirve para datos q no se guardara en la db
>>>>>>> usuario
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    //ver todas las personas
    public List<Persona> findAll(){
        return personaRepository.findAll();
    }

    //buscar por id
    //public Persona findById(Long id){
    //    return personaRepository.findById(id).get();
    //}

    // buscar por id con excepcion
    public Persona findByIdOrThrow(Long id){
        return personaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));
    }

    //crear y updatear
    public Persona save(Persona persona){
        return personaRepository.save(persona);
    }

<<<<<<< HEAD
    //updatear
=======
    //updatear por run
>>>>>>> usuario
    public Persona updatePersona(String run, Persona nPersona){
        Persona persona= findByRun(run);
        if(persona!=null){
            persona.setRun(nPersona.getRun());
            persona.setDvRun(nPersona.getDvRun());
            persona.setPNombre(nPersona.getPNombre());
            persona.setSNombre(nPersona.getSNombre());
            persona.setApPaterno(nPersona.getApPaterno());
            persona.setApMaterno(nPersona.getApMaterno());
            persona.setDireccion(nPersona.getDireccion());
            persona.setCorreo(nPersona.getCorreo());
            persona.setRol(nPersona.getRol());
<<<<<<< HEAD
            //persona.setComuna(nPersona.getComuna());
=======
            persona.setSexo(nPersona.getSexo());
            persona.setComunaId(nPersona.getComunaId());
>>>>>>> usuario
            return personaRepository.save(persona);
        }
        return null;
    }

    //borrar
    public void delete(Long id){
        personaRepository.deleteById(id);
    }

    //buscar por run
<<<<<<< HEAD
    public Persona findByRun(String run){
        List<Persona> personas=
        personaRepository.findByRun(run);
        if(!personas.isEmpty()){
            return personas.get(0);
        }
        return null;
    }

        // Verificar si existe por RUN
=======
    public Persona findByRun(String run) {
        return personaRepository.findByRun(run).orElse(null);
    }

    // Verificar si existe por RUN
>>>>>>> usuario
    public boolean existsByRun(String run){
        return personaRepository.existsByRun(run);
    }

    //buscar por apellido
    public List<Persona> findByApPaterno(String apPaterno){
        return personaRepository.findByApPaterno(apPaterno);
    }

    //buscar por rol
<<<<<<< HEAD
    public List<Persona> findByRol(String rol){
        return personaRepository.findByRol(rol);
    }

    // Contar por rol
    public long countByRol(String rol){
        return personaRepository.findByRol(rol).size();
=======
    public List<Persona> findByRol(Rol rol){
        return personaRepository.findByRol(rol);
    }

    // Buscar personas por sexo
    public List<Persona> findBySexo(Sexo sexo) {
        return personaRepository.findBySexo(sexo);
>>>>>>> usuario
    }

}
