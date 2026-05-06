package com.example.ejemplar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ejemplar.model.EjemplarEditorial;
import com.example.ejemplar.model.EjemplarEditorialID;

public interface EjemplarEditorialRepository extends JpaRepository<EjemplarEditorial, EjemplarEditorialID>{

}
