package com.biblioteca.comuna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.biblioteca.comuna.model.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    Optional<Comuna> findByNombre(String nombre);

    boolean existsByNombre(String nombre);


    
}
