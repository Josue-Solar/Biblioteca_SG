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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.service.SexoService;

@RestController
@RequestMapping("/api/v1/sexos")
public class SexoController {

    private static final Logger logger = LoggerFactory.getLogger(SexoController.class.getName());

    @Autowired
    private SexoService sexoService;

    // Listar todos los sexos
    @GetMapping
    public ResponseEntity<List<Sexo>> listar() {
        logger.info("Recibiendo solicitud para listar sexos");//log
        List<Sexo> sexos = sexoService.findAll();
        if (sexos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sexos);
    }

    // Crear nuevo sexo
    @PostMapping
    public ResponseEntity<Sexo> guardar(@RequestBody Sexo sexo) {
        logger.info("Recibiendo solicitud para guardar sexo");//log
        Sexo nSexo = sexoService.save(sexo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nSexo);
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Sexo> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar sexo por ID: " + id);//log
        try {
            Sexo sexo = sexoService.findByIdOrThrow(id);
            return ResponseEntity.ok(sexo);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Sexo> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar sexo por nombre: " + nombre);//log
        return sexoService.findByNombre(nombre)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ELIMINADO - No permitimos eliminar sexos para evitar referencias inválidas
    // @DeleteMapping("/{id}")

}
