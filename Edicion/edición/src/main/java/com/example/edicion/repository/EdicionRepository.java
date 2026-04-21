package com.example.edicion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.edicion.model.Edicion;
import java.util.List;


@Repository
public interface EdicionRepository extends JpaRepository<Edicion, Long>{

    @Query("SELECT e FROM Edicion e WHERE e.id= :id")
    List<Edicion> findById(long id);

}
