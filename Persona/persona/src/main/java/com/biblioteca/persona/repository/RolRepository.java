package com.biblioteca.persona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.persona.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long>{

    Optional<Rol> findByNombre(String nombre);
}
