package com.biblioteca.ejemplar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.ejemplar.model.EjemplarEdicion;
import com.biblioteca.ejemplar.model.EjemplarEdicionID;

public interface EjemplarEdicionRepository extends JpaRepository<EjemplarEdicion, EjemplarEdicionID>{

}
