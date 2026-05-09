package com.biblioteca.persona.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.service.impl.RolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/personas/roles")
public class RolController {

    private static final Logger logger = LoggerFactory.getLogger(RolController.class.getName());

    @Autowired
    private RolService rolService;

    // Listar todos los roles
    @GetMapping
    public ResponseEntity<List<Rol>> listar() {
        logger.info("Recibiendo solicitud para listar roles");//log
        List<Rol> roles = rolService.findAll();
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    // Crear nuevo rol
    @PostMapping
    public ResponseEntity<Rol> guardar(@Valid @RequestBody Rol rol) {
        logger.info("Recibiendo solicitud para guardar rol");//log
        Rol nrol = rolService.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nrol);
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Rol> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar rol por ID: " + id);//log
        try {
            Rol rol = rolService.findByIdOrThrow(id);
            return ResponseEntity.ok(rol);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Rol> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar rol por NOMBRE: " + nombre);//log
        return rolService.findByNombre(nombre)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<Rol> actualizar(@PathVariable Long id, @Valid @RequestBody Rol rol) {
        logger.info("Recibiendo solicitud para actualizar rol por ID: " + id);
        Rol rolActualizado = rolService.updateRol(id, rol);  
        if (rolActualizado != null) {
            return ResponseEntity.ok(rolActualizado);
        }
        return ResponseEntity.notFound().build();
    }

}
