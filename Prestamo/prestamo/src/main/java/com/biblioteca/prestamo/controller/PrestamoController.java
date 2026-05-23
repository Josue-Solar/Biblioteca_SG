package com.biblioteca.prestamo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.prestamo.dto.PrestamoDTO;
import com.biblioteca.prestamo.service.PrestamoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoController.class.getName());

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping
    public ResponseEntity<List<PrestamoDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar prestamos");
        return ResponseEntity.ok(prestamoService.findAll());
    }

    @PostMapping
    public ResponseEntity<PrestamoDTO.Response> guardar(@Valid @RequestBody PrestamoDTO.Request request){
        logger.info("Recibiendo solicitud para guardar prestamo");
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar prestamos por ID");
        prestamoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PrestamoDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar prestamos por ID");
        return ResponseEntity.ok(prestamoService.findByIdOrThrow(id));
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<PrestamoDTO.Response>> buscarPorPersona(@PathVariable Long personaId) {
        logger.info("Recibiendo solicitud para buscar prestamos por el ID de la PERSONA");
        return ResponseEntity.ok(prestamoService.findByPersonaId(personaId));
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<PrestamoDTO.Response>> buscarPrestamosAtrasados() {
        logger.info("Recibiendo solicitud para buscar prestamos atrasados");
        return ResponseEntity.ok(prestamoService.findPrestamosAtrasados());
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<PrestamoDTO.Response> registrarDevolucion(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para registrar la devolucion por ID");
        return ResponseEntity.ok(prestamoService.registrarDevolucion(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PrestamoDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody PrestamoDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar prestamo por ID");
        return ResponseEntity.ok(prestamoService.updatePrestamo(id, request));
    }
}