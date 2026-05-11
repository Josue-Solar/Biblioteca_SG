package com.example.libro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.libro.model.LibroGenero;
import com.example.libro.model.LibroGeneroID;

@Repository
public interface LibroGeneroRepository extends JpaRepository<LibroGenero, LibroGeneroID>{

    List<LibroGenero> findAllByGeneroId(Long generoId);
    List<LibroGenero> findAllByLibroIsbn(Long isbn);
    Void deleteByGeneroId(Long generoId);
}
