package com.biblioteca.persona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.persona.model.Sexo;

@Repository
public interface SexoRepository extends JpaRepository<Sexo, Long>{

        // Buscar por nombre del sexo
    Optional<Sexo> findByNombre(String nombre);
    
    // Verificar si ya existe un sexo con ese nombre
    boolean existsByNombre(String nombre);

}
