package com.biblioteca.persona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import com.biblioteca.persona.model.Persona;
=======
import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Sexo;
>>>>>>> usuario

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {


    //ver persona por nombre
    List<Persona> findByPNombre(String pNombre);

    //ver persona por apellido
    List<Persona> findByApPaterno(String apPaterno);

    //ver por mail
    Persona findByCorreo(String correo);

    //buscar por rol
<<<<<<< HEAD
    List<Persona> findByRol(String rol);
=======
    List<Persona> findByRol(Rol rol);
>>>>>>> usuario

    // Verificar si existe por RUN 
    boolean existsByRun(String run);

    //buscar por run
<<<<<<< HEAD
    List<Persona> findByRun(String run);
=======
    Optional<Persona> findByRun(String run);
    
    //buscar por sexo
    List<Persona> findBySexo(Sexo sexo);
>>>>>>> usuario

}
