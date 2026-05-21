package com.biblioteca.reserva.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.reserva.dto.ReservaDTO;
import com.biblioteca.reserva.service.ReservaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class.getName());

    @Autowired
    private ReservaService reservaService;

    //crud basico
    @GetMapping
    public ResponseEntity<List<ReservaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar prestamos");
        return ResponseEntity.ok(reservaService.findAll());
    }

    //crud basico
    @PostMapping
    public ResponseEntity<ReservaDTO.Response> guardar(@Valid @RequestBody ReservaDTO.Request request){
        logger.info("Recibiendo solicitud para guardar prestamo");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(request));
    }

    //crud basico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar prestamos por ID");
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //crud basico
    @GetMapping("/id/{id}")
    public ResponseEntity<ReservaDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar prestamos por ID");
        return ResponseEntity.ok(reservaService.findByIdOrThrow(id));
    }

    //busqueda especifica
    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<ReservaDTO.Response>> buscarPorPersona(@PathVariable Long personaId) {
        logger.info("Recibiendo solicitud para buscar prestamos por el ID de la PERSONA");
        return ResponseEntity.ok(reservaService.findByPersonaId(personaId));
    }

    // mas metodos

    // CRUD BÁSICO
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar reserva con ID: {}", id);
        return ResponseEntity.ok(reservaService.updateReserva(id, request));
    }

    // BÚSQUEDA ESPECÍFICA
    @GetMapping("/ejemplar/{ejemplarId}")
    public ResponseEntity<List<ReservaDTO.Response>> buscarPorEjemplar(@PathVariable Long ejemplarId) {
        logger.info("Recibiendo solicitud para buscar reservas por el ID del EJEMPLAR: {}", ejemplarId);
        return ResponseEntity.ok(reservaService.findByEjemplarId(ejemplarId));
    }

    // BÚSQUEDA ESPECÍFICA
    @GetMapping("/activas")
    public ResponseEntity<List<ReservaDTO.Response>> buscarActivas() {
        logger.info("Recibiendo solicitud para listar reservas ACTIVAS");
        return ResponseEntity.ok(reservaService.findReservasActivas());
    }

    // REGLA DE NEGOCIO
    @PutMapping("/{id}/retiro")
    public ResponseEntity<ReservaDTO.Response> registrarRetiro(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para registrar el retiro de la reserva ID: {}", id);
        return ResponseEntity.ok(reservaService.registrarRetiro(id));
    }

    // REGLA DE NEGOCIO
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO.Response> cancelarReserva(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para cancelar la reserva ID: {}", id);
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }

    // REGLA DE NEGOCIO
    @PostMapping("/procesar-expiradas")
    public ResponseEntity<Void> procesarExpiradas() {
        logger.info("Recibiendo solicitud manual para procesar y limpiar reservas expiradas");
        reservaService.procesarReservasExpiradas();
        return ResponseEntity.ok().build(); 
    }

}
