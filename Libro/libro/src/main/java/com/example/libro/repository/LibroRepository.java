package com.example.libro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.libro.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

}
