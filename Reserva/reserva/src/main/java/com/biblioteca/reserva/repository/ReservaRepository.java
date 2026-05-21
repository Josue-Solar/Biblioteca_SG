package com.biblioteca.reserva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.biblioteca.reserva.model.EstadoReserva;
import com.biblioteca.reserva.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>  {

    //ver reserva por id persona
    List<Reserva> findByPersonaId(Long personaId);

    //ver reserva por isbn ejemplar
    List<Reserva> findByEjemplarId(Long ejemplarId);

    //repo de los nuevos atributos

    // Buscar reservas activas (no completadas ni canceladas)
    List<Reserva> findByEstado(EstadoReserva estado);

    // Buscar reservas activas de una persona
    List<Reserva> findByPersonaIdAndEstado(Long personaId, String estado);

    // Buscar reservas expiradas (fechaExpiracion pasada y estado ACTIVA)
    @Query("SELECT r FROM Reserva r WHERE r.estado = :estado AND r.fechaExpiracion < CURRENT_DATE")
    List<Reserva> findReservasExpiradas(@Param("estado")EstadoReserva estado);

    // Verificar si una persona tiene reserva activa para un ejemplar
    boolean existsByPersonaIdAndEjemplarIdAndEstado(Long personaId, Long ejemplarId, EstadoReserva estado);

    // Contar reservas activas de una persona
    long countByPersonaIdAndEstado(Long personaId, String estado);

    // Verificar si CUALQUIER persona tiene reservado este ejemplar específico
    boolean existsByEjemplarIdAndEstado(Long ejemplarId, EstadoReserva estado);
}
