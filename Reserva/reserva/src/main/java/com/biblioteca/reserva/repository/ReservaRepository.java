package com.biblioteca.reserva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.reserva.model.Reserva;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEjemplarID(long id);
    List<Reserva> findByPersonaID(long id);
}
