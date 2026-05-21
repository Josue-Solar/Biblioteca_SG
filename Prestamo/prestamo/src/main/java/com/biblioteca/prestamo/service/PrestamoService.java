package com.biblioteca.prestamo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.prestamo.model.Prestamo;
import com.biblioteca.prestamo.repository.PrestamoRepository;
import com.biblioteca.prestamo.dto.PrestamoDTO;
import com.biblioteca.prestamo.dto.PersonaDTO;
import com.biblioteca.prestamo.dto.EjemplarDTO;
import com.biblioteca.prestamo.client.PersonaClient;
import com.biblioteca.prestamo.client.EjemplarClient;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    // 1. INYECTAMOS LOS FEIGN CLIENTS
    @Autowired
    private PersonaClient personaClient;

    @Autowired
    private EjemplarClient ejemplarClient;


    // MÉTODOS PÚBLICOS

    // Crear un préstamo
    public PrestamoDTO.Response crear(PrestamoDTO.Request request) {
        // Validar si el ejemplar ya está prestado antes de hacer nada
        if (isEjemplarPrestado(request.getEjemplarId())) {
            throw new RuntimeException("Ejemplar ID " + request.getEjemplarId() + "esta prestado.");
        }

        // Validar que la persona y el ejemplar existan en los otros microservicios
        validarExistencia(request.getPersonaId(), request.getEjemplarId()); //esta se comenta para q funcione solo
    
        // Convertir el Request (DTO) a Entidad para guardarlo en la BD
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaInicio(request.getFechaInicio());
        prestamo.setFechaFin(request.getFechaFin());
        prestamo.setPersonaId(request.getPersonaId());
        prestamo.setEjemplarId(request.getEjemplarId());

        Prestamo guardado = prestamoRepository.save(prestamo);

        // Devolver el recibo enriquecido
        return mapToResponse(guardado);
    }

    // Buscar por ID con excepción
    public PrestamoDTO.Response findByIdOrThrow(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
        return mapToResponse(prestamo);
    }

    // Ver todos los préstamos
    public List<PrestamoDTO.Response> findAll() {
        return prestamoRepository.findAll()
                .stream()
                .map(this::mapToResponse) // Transforma cada Entidad en un DTO Response
                .collect(Collectors.toList());
    }

    // Update por ID
    public PrestamoDTO.Response updatePrestamo(Long id, PrestamoDTO.Request request) {
        Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));

        // Solo validamos existencia si cambiaron los IDs
        if (!prestamo.getPersonaId().equals(request.getPersonaId()) || 
            !prestamo.getEjemplarId().equals(request.getEjemplarId())) {
            validarExistencia(request.getPersonaId(), request.getEjemplarId()); //esta se comenta para q funcione solo
        }

        prestamo.setFechaInicio(request.getFechaInicio());
        prestamo.setFechaFin(request.getFechaFin());
        prestamo.setPersonaId(request.getPersonaId());
        prestamo.setEjemplarId(request.getEjemplarId());

        Prestamo actualizado = prestamoRepository.save(prestamo);
        return mapToResponse(actualizado);
    }

    // Borrar
    public void delete(Long id) {
        prestamoRepository.deleteById(id);
    }

    // Registrar devolución
    public PrestamoDTO.Response registrarDevolucion(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
        
        prestamo.setFechaDevolucion(LocalDate.now());
        Prestamo guardado = prestamoRepository.save(prestamo);
        return mapToResponse(guardado);
    }

    // Buscar prestamos de una persona
    public List<PrestamoDTO.Response> findByPersonaId(Long personaId) {
        return prestamoRepository.findByPersonaId(personaId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Buscar prestamos activos (no devueltos aún)
    public List<PrestamoDTO.Response> findPrestamosActivos() {
        return prestamoRepository.findByFechaDevolucionIsNull()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Verificar si un ejemplar está actualmente prestado (Este retorna boolean, se queda igual)
    public boolean isEjemplarPrestado(Long ejemplarId) {
        return prestamoRepository.existsByEjemplarIdAndFechaDevolucionIsNull(ejemplarId);
    }

    // Buscar préstamos atrasados hasta hoy
    public List<PrestamoDTO.Response> findPrestamosAtrasados() {
        return prestamoRepository.findPrestamosAtrasados()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ====================================================================
    // MÉTODOS PRIVADOS (Helpers)
    // ====================================================================

    /**
     * Valida que la Persona y el Ejemplar existan llamando a los otros MS.
     * Si Feign no los encuentra (error 404), lanzará una excepción automáticamente.
     */
    private void validarExistencia(Long personaId, Long ejemplarId) {
        try {
            // Asumimos que los clients tienen un método "obtenerPorId"
            personaClient.obtenerPorId(personaId); 
            ejemplarClient.obtenerPorId(ejemplarId);
        } catch (Exception e) {
            log.error("Error al validar con microservicios externos: {}", e.getMessage());
            throw new RuntimeException("La persona o el ejemplar no existen en los registros.");
        }
    }

    /**
     * Transforma una Entidad Prestamo en un DTO Response enriquecido con Feign.
     */
    private PrestamoDTO.Response mapToResponse(Prestamo prestamo) {
        PrestamoDTO.Response response = new PrestamoDTO.Response();
        response.setId(prestamo.getId());
        response.setFechaInicio(prestamo.getFechaInicio());
        response.setFechaFin(prestamo.getFechaFin());
        response.setFechaDevolucion(prestamo.getFechaDevolucion());
    // --- CÁLCULO SEGURO DE ATRASADO ---
    boolean estaAtrasado = false;
    if (prestamo.getFechaFin() != null) {
        if (prestamo.getFechaDevolucion() != null) {
            // Si ya se devolvió, revisamos si la devolución fue después de la fecha fin
            estaAtrasado = prestamo.getFechaDevolucion().isAfter(prestamo.getFechaFin());
        } else {
            // Si aún no se devuelve, está atrasado si hoy ya pasó la fecha fin
            estaAtrasado = java.time.LocalDate.now().isAfter(prestamo.getFechaFin());
        }
    }
            response.setAtrasado(estaAtrasado);
        // Traemos la información humana desde los otros microservicios
        try {
            PersonaDTO persona = personaClient.obtenerPorId(prestamo.getPersonaId());
            response.setPersona(persona);

            EjemplarDTO ejemplar = ejemplarClient.obtenerPorId(prestamo.getEjemplarId());
            response.setEjemplar(ejemplar);
        } catch (Exception e) {
            log.warn("No se pudo obtener el detalle de Persona/Ejemplar para el préstamo ID: {}", prestamo.getId());
            // Si el otro MS está caído, devolvemos el préstamo igual pero sin los datos enriquecidos.
        }
        return response;
    }
}