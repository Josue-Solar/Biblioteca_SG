package com.biblioteca.ejemplar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.ejemplar.model.Ejemplar;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long>{
    Ejemplar getById(Long id);
    List<Ejemplar> getAllByLibroIsbn(Long isbn);
}
