package com.biblioteca.ejemplar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.ejemplar.model.EjemplarEditorial;
import com.biblioteca.ejemplar.model.EjemplarEditorialID;

public interface EjemplarEditorialRepository extends JpaRepository<EjemplarEditorial, EjemplarEditorialID>{

}
