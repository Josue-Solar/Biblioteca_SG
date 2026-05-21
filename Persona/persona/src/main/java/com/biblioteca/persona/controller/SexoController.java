package com.biblioteca.persona.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.service.SexoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/personas/sexo")
public class SexoController {

    private static final Logger logger = LoggerFactory.getLogger(SexoController.class.getName());

    @Autowired
    private SexoService sexoService;

    // Listar todos los sexos
    @GetMapping
    public ResponseEntity<?> listar() {
        logger.info("Recibiendo solicitud para listar sexos");//log
        List<SexoDTO.Response> sexos = sexoService.findAll();
        if (sexos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sexos);
    }

    // Crear nuevo sexo
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody SexoDTO.Request sexo) {
        logger.info("Recibiendo solicitud para guardar sexo");//log
        SexoDTO.Response nSexo = sexoService.save(sexo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nSexo);
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar sexo por ID: " + id);//log
        try {
            SexoDTO.Response sexo = sexoService.findByIdOrThrow(id);
            return ResponseEntity.ok(sexo);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar sexo por nombre: " + nombre);//log
        return ResponseEntity.ok(sexoService.findByNombre(nombre));
    }

    // ELIMINADO - No permitimos eliminar sexos para evitar referencias inválidas
    // @DeleteMapping("/{id}")

    //  metodo PUT actualizar
    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody SexoDTO.Request sexo) {
        logger.info("Recibiendo solicitud para actualizar sexo por NOMBRE: " + id);
        SexoDTO.Response sexoActualizado = sexoService.updateSexo(id, sexo);  
        if (sexoActualizado != null) {
            return ResponseEntity.ok(sexoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

}
