package com.example.genero.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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

import com.example.genero.model.Genero;
import com.example.genero.service.GeneroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/generos")
public class GeneroController {

    private static final Logger logger = Logger.getLogger(GeneroController.class.getName());
    
    @Autowired
    private GeneroService generoService;

    @GetMapping
    public ResponseEntity<Object> listar(){
        logger.info("Recibiendo solicitud para listar generos");//log
        List<Genero> generos = generoService.obtenerTodos();
        if(generos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Long> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar genero por ID: " + id);//log
        Optional<Genero> generos = generoService.obtenerPorId(id);
        if (generos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping
    public ResponseEntity<Genero> crear(@RequestBody Genero genero) {
        logger.info("Recibiendo solicitud para crear genero");//log
        Genero nuevoGenero = generoService.guardar(genero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGenero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genero> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Genero datos) {
        logger.info("Recibiendo solicitud para actualizar genero");
        return generoService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar genero por ID: " + id);//log
        try {
            generoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
