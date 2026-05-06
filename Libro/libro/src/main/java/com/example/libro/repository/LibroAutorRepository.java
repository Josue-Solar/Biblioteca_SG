package com.example.libro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.libro.model.LibroAutor;
import com.example.libro.model.LibroAutorID;

@Repository
public interface LibroAutorRepository extends JpaRepository<LibroAutor, LibroAutorID>{
    List<LibroAutor> findAllByLibroIsbn(LibroAutorID libroIsbn);
    Optional<LibroAutor> findById(LibroAutorID autorId);
    List<LibroAutor> findByLibroIsbn(Long libroIsbn);
}
