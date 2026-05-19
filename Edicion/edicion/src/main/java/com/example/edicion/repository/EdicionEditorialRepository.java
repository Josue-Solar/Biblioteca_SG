package com.example.edicion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.edicion.model.EdicionEditorial;
import com.example.edicion.model.EdicionEditorialID;

@Repository
public interface EdicionEditorialRepository extends JpaRepository<EdicionEditorial, EdicionEditorialID>{
    List<EdicionEditorial> findAllByEdicionIsbn(Long edicionId);
    List<EdicionEditorial> findAllByEditorialId(Long editorialId);
    List<EdicionEditorial> findByEdicionId(Long edicionId);
}
