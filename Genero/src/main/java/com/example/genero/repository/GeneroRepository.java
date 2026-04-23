package com.example.genero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.genero.model.Genero;

@Repository
public interface GeneroRepository extends  JpaRepository<Genero, Long> {

    @Query("SELECT g FROM Genero g ")
    List<Genero> findAll();

    @Query("SELECT g FROM Genero g WHERE g.id = :id")
    Optional<Genero> findById(Long id);

    @Query("SELECT g FROM Genero g WHERE LOWER(g.nombre)")
    List<Genero> findByNombre(String nombre);
}
