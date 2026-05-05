package com.biblioteca.comuna.controller;

import java.util.List;
import java.util.Optional;

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

import com.biblioteca.comuna.model.Comuna;
import com.biblioteca.comuna.service.ComunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    private static final Logger logger = LoggerFactory.getLogger(ComunaController.class.getName());

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<Comuna>> listar() {
        logger.info("Recibiendo solicitud para listar comunas");//log
        List<Comuna> comunas = comunaService.findAll();
        if(comunas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @PostMapping
    public ResponseEntity<Comuna> guardar(@Valid @RequestBody Comuna comuna){
        logger.info("Recibiendo solicitud para guardar comuna");//log
            Comuna nComuna = comunaService.save(comuna);
            return ResponseEntity.status(HttpStatus.CREATED).body(nComuna);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar comuna por id");//log
        try{
            comunaService.delete(id);
            return ResponseEntity.noContent().build();
        }catch(Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Comuna> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar comuna por id");//log
        try {
            Comuna comuna = comunaService.findByIdOrThrow(id);
            return ResponseEntity.ok(comuna);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Comuna> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar comunas por nombre");//log
        Optional<Comuna> comuna = comunaService.findByNombre(nombre);
        return comuna
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<Comuna> actualizar(@PathVariable Long id, @Valid @RequestBody Comuna comuna) {
        logger.info("Recibiendo solicitud para actualizar Comuna por ID: " + id);
        Comuna comunaActualizado = comunaService.updateComuna(id, comuna);  
        if (comunaActualizado != null) {
            return ResponseEntity.ok(comunaActualizado);
        }
        return ResponseEntity.notFound().build();
    }

}
