package com.example.libro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.libro.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByIsbn(Long isbn);
    List<Libro> findByNombre(String nombre);
    Optional<Boolean> deleteLibroByIsbn(Long isbn);
}
