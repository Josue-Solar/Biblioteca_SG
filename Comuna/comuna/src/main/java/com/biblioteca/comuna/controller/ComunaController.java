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

import com.biblioteca.comuna.dto.ComunaDTO;

import com.biblioteca.comuna.service.ComunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    private static final Logger logger = LoggerFactory.getLogger(ComunaController.class.getName());

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar comunas");//log
        List<ComunaDTO.Response> comunas = comunaService.findAll();
        if(comunas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @PostMapping
    public ResponseEntity<ComunaDTO.Response> guardar(@Valid @RequestBody ComunaDTO.Request request){
        logger.info("Recibiendo solicitud para guardar comuna");//log
            ComunaDTO.Response response = comunaService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public ResponseEntity<ComunaDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar comuna por id");//log
        try {
            ComunaDTO.Response response = comunaService.findByIdOrThrow(id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ComunaDTO.Response> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar comunas por nombre");//log
        Optional<ComunaDTO.Response> response = comunaService.findByNombre(nombre);
        return response
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<ComunaDTO.Response> 
            actualizar(@PathVariable Long id, @Valid @RequestBody ComunaDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Comuna por ID: " + id);
        return ResponseEntity.ok(comunaService.update(id, request));
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<ComunaDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody ComunaDTO.Request request) {
    logger.info("Recibiendo solicitud para actualizar Comuna por ID: {}", id);
    try {
        ComunaDTO.Response response = comunaService.update(id, request);
        
        // Si todo sale bien, devolvemos 200 OK
        return ResponseEntity.ok(response);
    } catch (Exception ex) {
        // Si el service lanzó el RuntimeException, caemos aquí y devolvemos 404
        logger.error("Error al actualizar la comuna: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
        }
    } */

}
