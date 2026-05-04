package com.example.editorial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.editorial.model.Editorial;

@Repository
public interface EditorialRepository extends  JpaRepository<Editorial, Long> {

    @Query("SELECT e FROM Editorial e ")
    List<Editorial> findAll();

    @Query("SELECT e FROM Editorial e WHERE e.id = :id")
    Optional<Editorial> findById(Long id);

    @Query("SELECT e FROM Editorial e WHERE LOWER(e.nombre)")
    List<Editorial> findByNombre(String nombre);

}
