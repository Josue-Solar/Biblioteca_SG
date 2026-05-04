package com.example.edicion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.edicion.model.Edicion;
import java.util.List;
import java.util.Optional;


@Repository
public interface EdicionRepository extends JpaRepository<Edicion, Long>{

    @Query("SELECT e FROM Edicion e ")
    List<Edicion> findAll();

    @Query("SELECT e FROM Edicion e WHERE e.id = :id")
    Optional<Edicion> findById(Long id);

    @Query("SELECT e FROM Edicion e WHERE LOWER(e.nombre)")
    List<Edicion> findByNombre(String nombre);

}
