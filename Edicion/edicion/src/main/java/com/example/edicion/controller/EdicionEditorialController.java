package com.example.edicion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edicion.dto.EdicionEditorialDTO;
import com.example.edicion.service.EdicionEditorialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/edicionEditoriales")
public class EdicionEditorialController {
    @Autowired
    private EdicionEditorialService edicionEditorialService;

    @GetMapping 
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(edicionEditorialService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<?> addEdicionEditorial(@Valid @RequestBody EdicionEditorialDTO.Request request){
        return ResponseEntity.ok(edicionEditorialService.guardar(request));
    }
}
