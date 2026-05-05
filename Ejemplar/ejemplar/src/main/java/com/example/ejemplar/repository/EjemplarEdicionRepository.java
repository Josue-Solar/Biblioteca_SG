package com.example.ejemplar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ejemplar.model.EjemplarEdicion;
import com.example.ejemplar.model.EjemplarEdicionID;

public interface EjemplarEdicionRepository extends JpaRepository<EjemplarEdicion, EjemplarEdicionID>{

}
