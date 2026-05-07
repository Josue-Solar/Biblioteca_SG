package com.biblioteca.ejemplar.controller;

import java.util.List;

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

import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.service.EjemplarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ejemplares")
@RequiredArgsConstructor
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @GetMapping
    public List<Ejemplar> getAllEjemplares(){
        return ejemplarService.obtenerTodos();
    }

    @GetMapping("/id:{id}")
    public ResponseEntity<Ejemplar> getByID(@Valid @PathVariable long id){
        return ejemplarService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/traeLibro/{id}")
    public ResponseEntity<?> getLibro(@PathVariable Long id){
        return ResponseEntity.ok(ejemplarService.getLibro(id));
    }

    @PostMapping
    public ResponseEntity<Ejemplar> saveEntity(@Valid @RequestBody Ejemplar ejemplar){
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarService.guardar(ejemplar));
    }
    
    @PutMapping("actualizar/{id}")
    public ResponseEntity<?> updateEjemplar(@PathVariable Long id, @Valid @RequestBody Ejemplar ejemplar){
        return ResponseEntity.ok(ejemplarService.modReserva(id, ejemplar));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        ejemplarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
