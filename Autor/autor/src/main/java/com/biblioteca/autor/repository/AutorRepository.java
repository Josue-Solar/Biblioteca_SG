package com.biblioteca.autor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.autor.model.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    //ver autor por nombre
    List<Autor> findByPrimerNombreIgnoreCase(String primerNombre);

    //ver persona por apellido
    List<Autor> findByApPaternoIgnoreCase(String apPaterno);

    //buscar todos los que coincidan con nombre y apellido
    List<Autor> findAllByPrimerNombreAndApPaternoAllIgnoreCase(String primerNombre, String apPaterno);

}
