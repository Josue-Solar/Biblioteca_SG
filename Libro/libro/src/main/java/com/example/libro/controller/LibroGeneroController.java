package com.example.libro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.libro.dto.LibroGeneroDTO;
import com.example.libro.service.LibroGeneroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/libroGeneros/")
@RequiredArgsConstructor
public class LibroGeneroController {

    private final LibroGeneroService libroGeneroService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(libroGeneroService.obtenerTodos());
    }

    @GetMapping("/porGenero/{generoId}")
    public ResponseEntity<List<?>> getAllByGeneroId(@PathVariable Long generoId){
        return ResponseEntity.ok(libroGeneroService.obtenerPorGeneroId(generoId));
    }

    @PostMapping
    public ResponseEntity<?> agregarRegistro(@Valid @RequestBody LibroGeneroDTO.Request libroGenero){
        return ResponseEntity.ok(libroGeneroService.guardar(libroGenero));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminarPorId(@PathVariable long id){
        libroGeneroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody LibroGeneroDTO.Request nLibroGenero){
        return ResponseEntity.ok(libroGeneroService.actualizar(id, nLibroGenero));
    }
}
