package com.example.libro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.libro.dto.LibroAutorDTO;
import com.example.libro.service.LibroAutorService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/libroAutores")
public class LibroAutorController {
    @Autowired
    private LibroAutorService libroAutorService;

    @GetMapping 
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(libroAutorService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<?> addLibroAutor(@Valid @RequestBody LibroAutorDTO.Request request){
        return ResponseEntity.ok(libroAutorService.guardar(request));
    }

}
