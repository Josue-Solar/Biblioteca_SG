package com.biblioteca.genero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.genero.model.Genero;

@Repository
public interface GeneroRepository extends  JpaRepository<Genero, Long> {

    List<Genero> findByNombre(String nombre);

    List<Genero> findById(long id);
}
