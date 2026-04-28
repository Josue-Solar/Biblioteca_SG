package com.biblioteca.reserva.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.reserva.model.Reserva;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    
}
