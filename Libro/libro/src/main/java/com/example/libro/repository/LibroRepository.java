package com.example.libro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.libro.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Genero l WHERE LOWER(l.nombre)")
    List<Libro> findByNombre(String nombre);
}
