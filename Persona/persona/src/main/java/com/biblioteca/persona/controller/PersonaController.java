package com.biblioteca.persona.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.service.PersonaService;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> listar() {
        List<Persona> personas = personaService.findAll();
        if(personas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @PostMapping
    public ResponseEntity<Persona> 
        guardar(@RequestBody Persona persona){
            Persona nPersona = 
                personaService.save(persona);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(nPersona);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        try{
            personaService.delete(id);
            return ResponseEntity.noContent().build();
        }catch(Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
        try {
            Persona persona = personaService.findByIdOrThrow(id);
            return ResponseEntity.ok(persona);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    //Buscar por RUN (corregido)
    @GetMapping("/run/{run}")
    public ResponseEntity<Persona> buscarPorRun(@PathVariable String run) {
        Persona persona = personaService.findByRun(run);
        if(persona== null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persona);
    }
    

    //buscar por rol
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Persona>> findByRol(@PathVariable String rol){
        List<Persona> personas = personaService.findByRol(rol);
        if(personas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Persona>> buscarPorApellido(@PathVariable String apellido) {
        List<Persona> personas = personaService.findByApPaterno(apellido);
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }
    
    

}
