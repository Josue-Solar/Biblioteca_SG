package com.biblioteca.persona.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.SexoService;
import com.biblioteca.persona.service.impl.PersonaServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class.getName());

    @Autowired
    private PersonaServiceImpl personaService;

    @Autowired
    private SexoService sexoService;
    
    @Autowired
    private RolService rolService;

    @GetMapping //mostrar personas
    public ResponseEntity<List<PersonaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar personas");//log
        List<PersonaDTO.Response> personas = personaService.findAll();
        if(personas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @PostMapping //registrar persona, up: faltaba el @valid
    public ResponseEntity<PersonaDTO.Response> guardar(@Valid @RequestBody PersonaDTO.Request persona){
        logger.info("Recibiendo solicitud para guardar persona");//log
        PersonaDTO.Response nPersona = personaService.save(persona);
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
    @GetMapping("/id:{id}")
    public ResponseEntity<PersonaDTO.Response> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + id);//log
        try {
            PersonaDTO.Response persona = personaService.findById(id);
            return ResponseEntity.ok(persona);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    //Buscar por RUN (corregido)
    @GetMapping("/run/{run}")
    public ResponseEntity<PersonaDTO.Response> buscarPorRun(@PathVariable String run) {
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + run);//log
        PersonaDTO.Response persona = personaService.findByRun(run);
        if(persona== null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persona);
    }
    
    //buscar por rol
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorrol(@PathVariable Long rolId) {
        logger.info("Recibiendo solicitud para buscar persona por ROL: " + rolId);//log
        Rol rol = rolService.findByIdOrThrow(rolId);  
        List<PersonaDTO.Response> personas = personaService.findByRol(rol);
            return ResponseEntity.ok(personas);
    }

    @GetMapping("/apellido/{apellido}") //buscar por apellido
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorApellido(@PathVariable String apellido) {
        logger.info("Recibiendo solicitud para buscar persona por APELLIDO: " + apellido);//log
        List<PersonaDTO.Response> personas = personaService.findByApPaterno(apellido);
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/sexo/{sexoId}")
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorSexo(@PathVariable Long sexoId) {
        logger.info("Recibiendo solicitud para buscar persona por SEXO: " + sexoId);//log
        try {
            Sexo sexo = sexoService.findByIdOrThrow(sexoId);
            List<PersonaDTO.Response> personas = personaService.findBySexo(sexo);
            if (personas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(personas);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/comuna")
    public ResponseEntity<?> buscarPorComuna(@RequestParam String nombre){
        try{
            return ResponseEntity.ok(personaService.findByComunaNombre(nombre));
        }catch(Exception ex){
            ex.printStackTrace(); // ← agrega esto
            return ResponseEntity.status(500).body(ex.getMessage()); // ← cambia a 500 con mensaje
        }
    }

    @GetMapping("/comunaID/{id}")
    public ResponseEntity<?> buscarPorComunaID(@PathVariable Long id){
        try{
            return ResponseEntity.ok(personaService.findByComunaID(id));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage()); 
        }
    }


    //  metodo PUT actualizar
    @PutMapping("/{run}") // Actualizar por RUN
    public ResponseEntity<PersonaDTO.Response> actualizar(@PathVariable String run, @Valid @RequestBody PersonaDTO.Request persona) {
        logger.info("Recibiendo solicitud para actualizar persona por RUN: " + run);
        PersonaDTO.Response personaActualizada = personaService.updatePersona(run, persona);  
        if (personaActualizada != null) {
            return ResponseEntity.ok(personaActualizada);
        }
        return ResponseEntity.notFound().build();
    }
    
    

}
