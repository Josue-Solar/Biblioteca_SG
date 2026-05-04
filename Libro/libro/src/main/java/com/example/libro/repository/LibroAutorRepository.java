package com.example.libro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.libro.model.LibroAutor;
import com.example.libro.model.LibroAutorID;

@Repository
public interface LibroAutorRepository extends JpaRepository<LibroAutor, LibroAutorID>{

}
