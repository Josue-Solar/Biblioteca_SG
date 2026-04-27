package com.biblioteca.persona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.persona.model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long>{

    // Buscar por nombre del cargo
    Optional<Cargo> findByNombre(String nombre);

}
