package com.example.libro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.libro.model.LibroGenero;

@Repository
public interface LibroGeneroRepository extends JpaRepository<LibroGenero, Long>{

}
