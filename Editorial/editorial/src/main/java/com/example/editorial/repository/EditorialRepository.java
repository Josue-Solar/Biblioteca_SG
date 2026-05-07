package com.example.editorial.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.editorial.model.Editorial;

@Repository
public interface EditorialRepository extends  JpaRepository<Editorial, Long> {

    List<Editorial> findByNombre(String nombre);

    List<Editorial> findById(long id);  

}
