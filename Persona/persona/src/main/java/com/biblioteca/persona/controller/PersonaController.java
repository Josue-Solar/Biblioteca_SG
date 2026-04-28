package com.biblioteca.persona.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.biblioteca.persona.model.Cargo;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.service.CargoService;
import com.biblioteca.persona.service.PersonaService;
import com.biblioteca.persona.service.SexoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class.getName());

    @Autowired
    private PersonaService personaService;

    @Autowired
    private SexoService sexoService;
    
    @Autowired
    private CargoService cargoService;

    @GetMapping //mostrar personas
    public ResponseEntity<List<Persona>> listar() {
        logger.info("Recibiendo solicitud para listar personas");//log
        List<Persona> personas = personaService.findAll();
        if(personas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @PostMapping //registrar persona, up: faltaba el @valid
    public ResponseEntity<Persona> guardar(@Valid @RequestBody Persona persona){
        logger.info("Recibiendo solicitud para guardar persona");//log
        Persona nPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nPersona);
    }

    @DeleteMapping("/{id}") //borrar por id
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
    logger.info("Recibiendo solicitud para eliminar persona por ID: " + id);//log
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
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + id);//log
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
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + run);//log
        Persona persona = personaService.findByRun(run);
        if(persona== null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persona);
    }
    
    //buscar por cargo
    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<Persona>> buscarPorCargo(@PathVariable Long cargoId) {
        logger.info("Recibiendo solicitud para buscar persona por CARGO: " + cargoId);//log
        Cargo cargo = cargoService.findByIdOrThrow(cargoId);  
        List<Persona> personas = personaService.findByCargo(cargo);
            return ResponseEntity.ok(personas);
    }

    @GetMapping("/apellido/{apellido}") //buscar por apellido
    public ResponseEntity<List<Persona>> buscarPorApellido(@PathVariable String apellido) {
        logger.info("Recibiendo solicitud para buscar persona por APELLIDO: " + apellido);//log
        List<Persona> personas = personaService.findByApPaterno(apellido);
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

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
    
    

}
