package com.biblioteca.persona.controller;

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> usuario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.PutMapping;
>>>>>>> usuario
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.service.PersonaService;
=======
import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.PersonaService;
import com.biblioteca.persona.service.SexoService;

import jakarta.validation.Valid;
>>>>>>> usuario

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController {

<<<<<<< HEAD
    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> listar() {
=======
    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class.getName());

    @Autowired
    private PersonaService personaService;

    @Autowired
    private SexoService sexoService;
    
    @Autowired
    private RolService rolService;

    @GetMapping //mostrar personas
    public ResponseEntity<List<Persona>> listar() {
        logger.info("Recibiendo solicitud para listar personas");//log
>>>>>>> usuario
        List<Persona> personas = personaService.findAll();
        if(personas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

<<<<<<< HEAD
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
=======
    @PostMapping //registrar persona, up: faltaba el @valid
    public ResponseEntity<Persona> guardar(@Valid @RequestBody Persona persona){
        logger.info("Recibiendo solicitud para guardar persona");//log
        Persona nPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nPersona);
    }

    @DeleteMapping("/{id}") //borrar por id
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
    logger.info("Recibiendo solicitud para eliminar persona por ID: " + id);//log
>>>>>>> usuario
        try{
            personaService.delete(id);
            return ResponseEntity.noContent().build();
        }catch(Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por ID
<<<<<<< HEAD
    @GetMapping("/id/{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
=======
    @GetMapping("/id:{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + id);//log
>>>>>>> usuario
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
<<<<<<< HEAD
=======
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + run);//log
>>>>>>> usuario
        Persona persona = personaService.findByRun(run);
        if(persona== null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persona);
    }
    
<<<<<<< HEAD

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
=======
    //buscar por rol
    @GetMapping("/rol/{rold}")
    public ResponseEntity<List<Persona>> buscarPorrol(@PathVariable Long rolId) {
        logger.info("Recibiendo solicitud para buscar persona por ROL: " + rolId);//log
        Rol rol = rolService.findByIdOrThrow(rolId);  
        List<Persona> personas = personaService.findByRol(rol);
            return ResponseEntity.ok(personas);
    }

    @GetMapping("/apellido/{apellido}") //buscar por apellido
    public ResponseEntity<List<Persona>> buscarPorApellido(@PathVariable String apellido) {
        logger.info("Recibiendo solicitud para buscar persona por APELLIDO: " + apellido);//log
>>>>>>> usuario
        List<Persona> personas = personaService.findByApPaterno(apellido);
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }
<<<<<<< HEAD
=======

    @GetMapping("/sexo/{sexoId}")
    public ResponseEntity<List<Persona>> buscarPorSexo(@PathVariable Long sexoId) {
        logger.info("Recibiendo solicitud para buscar persona por SEXO: " + sexoId);//log
        try {
            Sexo sexo = sexoService.findByIdOrThrow(sexoId);
            List<Persona> personas = personaService.findBySexo(sexo);
            if (personas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(personas);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    //  metodo PUT actualizar
    @PutMapping("/{run}") // Actualizar por RUN
    public ResponseEntity<Persona> actualizar(@PathVariable String run, @Valid @RequestBody Persona persona) {
        logger.info("Recibiendo solicitud para actualizar persona por RUN: " + run);
        Persona personaActualizada = personaService.updatePersona(run, persona);  
        if (personaActualizada != null) {
            return ResponseEntity.ok(personaActualizada);
        }
        return ResponseEntity.notFound().build();
    }
>>>>>>> usuario
    
    

}
