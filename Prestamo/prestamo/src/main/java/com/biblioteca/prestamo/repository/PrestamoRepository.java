package com.biblioteca.prestamo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.biblioteca.prestamo.model.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{

    // Buscar préstamos por persona
    List<Prestamo> findByPersonaId(Long personaId);
    
    // Buscar préstamos por ejemplar
    List<Prestamo> findByEjemplarId(Long ejemplarId);
    
    // Buscar préstamos activos (sin fecha de devolución)
    List<Prestamo> findByFechaDevolucionIsNull();

    // Buscar préstamos atrasados (fechaFin pasada y sin devolver)
    List<Prestamo> findByFechaDevolucionIsNullAndFechaFinBefore(LocalDate fecha);

    // Verificar si un ejemplar está prestado actualmente
    boolean existsByEjemplarIdAndFechaDevolucionIsNull(Long ejemplarId);

    // Para búsqueda actual atrasados (siempre hoy)
    @Query("SELECT p FROM Prestamo p WHERE p.fechaDevolucion IS NULL AND p.fechaFin < CURRENT_DATE")
    List<Prestamo> findPrestamosAtrasados();

}
