package com.biblioteca.libro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.libro.model.LibroAutor;
import com.biblioteca.libro.model.LibroAutorID;

@Repository
public interface LibroAutorRepository extends JpaRepository<LibroAutor, LibroAutorID>{
    List<LibroAutor> findAllByLibroIsbn(Long libroIsbn);
    List<LibroAutor> findAllByAutorId(Long autorId);
    List<LibroAutor> findByLibroIsbn(Long libroIsbn);
}
