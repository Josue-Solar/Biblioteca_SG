package com.example.edicion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.edicion.model.Edicion;
import java.util.List;
import java.util.Optional;


@Repository
public interface EdicionRepository extends JpaRepository<Edicion, Long>{
    Optional<Edicion> findById(long id);
    List<Edicion> findByNombre(String nombre);
    Optional<Boolean> deleteEdicionById(long id);

}
