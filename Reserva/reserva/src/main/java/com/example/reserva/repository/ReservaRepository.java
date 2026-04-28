package com.example.reserva.repository;

import com.example.reserva.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAll();
    List<Reserva> findByPersonaId(@Param("id") long id);
    List<Reserva> findByEjemplarId(long id);
    Reserva save(Reserva res);
    void deleteById(long id);
}
