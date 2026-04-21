package com.biblioteca.persona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.persona.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {


    //ver persona por nombre
    List<Persona> findByPNombre(String pNombre);

    //ver persona por apellido
    List<Persona> findByApPaterno(String apPaterno);

    //ver por mail
    Persona findByCorreo(String correo);

    //buscar por rol
    List<Persona> findByRol(String rol);

    // Verificar si existe por RUN 
    boolean existsByRun(String run);

    //buscar por run
    List<Persona> findByRun(String run);

}
