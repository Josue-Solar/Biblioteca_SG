package com.example.libro.controller;

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

import com.example.libro.model.Libro;
import com.example.libro.service.LibroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @GetMapping
    public List<Libro> getAllLibs(){
        return libroService.obtenerTodos();
    }

    @GetMapping("/isbn:{isbn}")
    public ResponseEntity<?> getByID(@Valid @PathVariable long isbn){
        try{
            return ResponseEntity.ok(libroService.obtenerPorIsbn(isbn));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/autoresPorLibro:{isbn}")
    public ResponseEntity<?> getAutoresPorLibro(@PathVariable Long isbn){
        try{
            return ResponseEntity.ok(libroService.obtenerAutores(isbn));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/ejemplares/{isbn}")
    public ResponseEntity<?> getEjemplares(@PathVariable Long isbn) {
        return ResponseEntity.ok(libroService.listarEjemplares(isbn));
    }
    

    @GetMapping("/autorId/{id}")
    public ResponseEntity<List<?>> getAllByAuthId(@PathVariable Long id){
        return ResponseEntity.ok(libroService.listarLibros(id));
    }

    @PostMapping
    public ResponseEntity<Libro> addLibro(@Valid @RequestBody Libro lib){
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardar(lib));
    }

    @PutMapping("/editar:{isbn}")
    public ResponseEntity<Libro> putLibro(@Valid @RequestBody Libro lib, @PathVariable Long isbn){
        return libroService.actualizar(isbn, lib).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar:{isbn}")
    public ResponseEntity<Boolean> deleteLibro(@Valid @PathVariable Long isbn){
        return libroService.eliminar(isbn).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
