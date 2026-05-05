package com.biblioteca.autor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.autor.model.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    //ver autor por nombre
    List<Autor> findByPNombre(String pNombre);

    //ver persona por apellido
    List<Autor> findByApPaterno(String apPaterno);

    //buscar todos los que coincidan con nombre y apellido
    List<Autor> findAllByPNombreAndApPaterno(String pNombre, String apPaterno);

}
