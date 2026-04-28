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

import com.biblioteca.persona.model.Cargo;
import com.biblioteca.persona.service.CargoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cargos")
public class CargoController {

    private static final Logger logger = LoggerFactory.getLogger(CargoController.class.getName());

    @Autowired
    private CargoService cargoService;

    // Listar todos los cargos
    @GetMapping
    public ResponseEntity<List<Cargo>> listar() {
        logger.info("Recibiendo solicitud para listar cargos");//log
        List<Cargo> cargos = cargoService.findAll();
        if (cargos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cargos);
    }

    // Crear nuevo cargo
    @PostMapping
    public ResponseEntity<Cargo> guardar(@Valid @RequestBody Cargo cargo) {
        logger.info("Recibiendo solicitud para guardar cargo");//log
        Cargo nCargo = cargoService.save(cargo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nCargo);
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Cargo> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar cargo por ID: " + id);//log
        try {
            Cargo cargo = cargoService.findByIdOrThrow(id);
            return ResponseEntity.ok(cargo);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Cargo> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar cargo por NOMBRE: " + nombre);//log
        return cargoService.findByNombre(nombre)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<Cargo> actualizar(@PathVariable Long id, @Valid @RequestBody Cargo cargo) {
        logger.info("Recibiendo solicitud para actualizar cargo por ID: " + id);
        Cargo cargoActualizado = cargoService.updateCargo(id, cargo);  
        if (cargoActualizado != null) {
            return ResponseEntity.ok(cargoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

}
