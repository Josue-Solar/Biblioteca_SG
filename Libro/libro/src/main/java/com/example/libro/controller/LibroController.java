package com.example.libro.controller;

import java.util.ArrayList;
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

import com.example.libro.client.AutorClient;
import com.example.libro.dto.AutorDTO;
import com.example.libro.dto.LibroAutorDTO;
import com.example.libro.model.Libro;
import com.example.libro.model.LibroAutor;
import com.example.libro.repository.LibroAutorRepository;
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
    public ResponseEntity<Libro> getByID(@Valid @PathVariable long isbn){
        return libroService.obtenerPorIsbn(isbn).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/porAutores:{isbn}")
    public ResponseEntity<?> getOrderByAutores(@PathVariable Long isbn){
        try{
            return ResponseEntity.ok(libroService.obtenerAutores(isbn));
        }catch(Exception ex){
            ex.printStackTrace(); // ← agrega esto
            return ResponseEntity.status(500).body(ex.getMessage()); // ← cambia a 500 con mensaje
        }
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
