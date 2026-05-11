package com.biblioteca.editorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.editorial.model.EditorialEdicion;
import com.biblioteca.editorial.service.EditorialEdicionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/editorialEdiciones/")
@RequiredArgsConstructor
public class EditorialEdicionController {
    private final EditorialEdicionService editorialEdicionService;
    
    @PostMapping
    public ResponseEntity<?> agregarRegistro(@Valid @RequestBody EditorialEdicion editorialEdicion){
        return ResponseEntity.ok(editorialEdicionService.agregarRegistro(editorialEdicion));
    }
}
