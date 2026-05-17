package com.biblioteca.editorial.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.biblioteca.editorial.model.Editorial;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long>{

    Optional<Editorial> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

}
