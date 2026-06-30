package com.biblioteca.persona.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.SexoService;
import com.biblioteca.persona.service.impl.PersonaServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
@Tag(name = "Personas V1", description = "API para la gestión de personas en la versión 1")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class.getName());

    private final PersonaServiceImpl personaService;
    private final SexoService sexoService;
    private final RolService rolService;

    @GetMapping //mostrar personas
    @Operation(summary = "Obtener todas las personas", description = "Obtiene una lista de todas las personas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de personas obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay personas registradas en la base de datos")
    })
    public ResponseEntity<List<PersonaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar personas");//log
        List<PersonaDTO.Response> personas = personaService.findAll();
        if(personas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @PostMapping //registrar persona
    @Operation(summary = "Registrar nueva persona", description = "Crea un nuevo registro de persona en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Persona creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<PersonaDTO.Response> guardar(@Valid @RequestBody PersonaDTO.Request persona){
        logger.info("Recibiendo solicitud para guardar persona");//log
        PersonaDTO.Response nPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nPersona);
    }

    @DeleteMapping("/{id}") //borrar por id
    @Operation(summary = "Eliminar persona por ID", description = "Elimina un registro de persona basado en su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Persona eliminada con éxito (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id){
    logger.info("Recibiendo solicitud para eliminar persona por ID: " + id);//log
        try{
            personaService.delete(id);
            return ResponseEntity.noContent().build();
        }catch(Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por ID
    @GetMapping("/{id}") 
    @Operation(summary = "Buscar persona por ID", description = "Obtiene los detalles de una persona específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Persona encontrada"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<PersonaDTO.Response> buscarPorId(@PathVariable("id") long id) {
        logger.info("Recibiendo solicitud para buscar persona por RUT: " + id);//log
        try {
            PersonaDTO.Response persona = personaService.findById(id);
            return ResponseEntity.ok(persona);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método PUT actualizar
    @PutMapping("/{id}") // Actualizar por ID
    @Operation(summary = "Actualizar persona por ID", description = "Actualiza los datos de una persona existente buscando por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Persona actualizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada para actualizar")
    })
    public ResponseEntity<PersonaDTO.Response> actualizar(@PathVariable("id") Long id, @Valid @RequestBody PersonaDTO.Request persona) {
        logger.info("Recibiendo solicitud para actualizar persona por ID: " + id);
        try {
            PersonaDTO.Response personaActualizada = personaService.updatePersona(id, persona);  
            if (personaActualizada != null) {
                return ResponseEntity.ok(personaActualizada);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            // Atrapamos la excepción del test/servicio y devolvemos 404
            return ResponseEntity.notFound().build(); 
        }
    }

    //Buscar por RUN 
    @GetMapping("/run/{run}")
    @Operation(summary = "Buscar persona por RUN", description = "Obtiene los detalles de una persona específica por su RUN exacto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Persona encontrada"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<PersonaDTO.Response> buscarPorRun(@PathVariable("run") String run) { 
        logger.info("Recibiendo solicitud para buscar persona por RUN: " + run);
        try {
            PersonaDTO.Response persona = personaService.findByRun(run);
            if(persona == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(persona);
        } catch (Exception ex) {
            // Atrapamos la excepción del test/servicio y devolvemos 404
            return ResponseEntity.notFound().build();
        }
    }
    
    //buscar por rol
    @GetMapping("/rol/{rolId}")
    @Operation(summary = "Buscar personas por ID de Rol", description = "Obtiene una lista de personas que tienen asignado un rol específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personas encontradas para el rol"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorrol(@PathVariable("rolId") Long rolId) {
        logger.info("Recibiendo solicitud para buscar persona por ROL: " + rolId);//log
        RolDTO.Response rol = rolService.findByIdOrThrow(rolId);  
        List<PersonaDTO.Response> personas = personaService.findByRol(rol);
        return ResponseEntity.ok(personas);
    }

    // buscar por apellido
    @GetMapping("/apellido/{apellido}") //buscar por apellido
    @Operation(summary = "Buscar personas por Apellido Paterno", description = "Obtiene una lista de personas cuyo apellido paterno coincida con la búsqueda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personas encontradas"),
        @ApiResponse(responseCode = "204", description = "No se encontraron personas con ese apellido")
    })
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorApellido(@PathVariable("apellido") String apellido) {
        logger.info("Recibiendo solicitud para buscar persona por APELLIDO: " + apellido);//log
        List<PersonaDTO.Response> personas = personaService.findByApPaterno(apellido);
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    //buscar por sexo
    @GetMapping("/sexo/{sexoId}")
    @Operation(summary = "Buscar personas por ID de Sexo", description = "Obtiene una lista de personas filtradas por su identificador de sexo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personas encontradas"),
        @ApiResponse(responseCode = "204", description = "No se encontraron personas para este sexo"),
        @ApiResponse(responseCode = "404", description = "Sexo no encontrado")
    })
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorSexo(@PathVariable("sexoId") Long sexoId) {
        logger.info("Recibiendo solicitud para buscar persona por SEXO: " + sexoId);//log
        try {
            SexoDTO.Response sexo = sexoService.findByIdOrThrow(sexoId);
            List<PersonaDTO.Response> personas = personaService.findBySexo(sexo);
            if (personas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(personas);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // buscar por nombre de comuna
    @GetMapping("/comuna")
    @Operation(summary = "Buscar personas por Nombre de Comuna", description = "Obtiene una lista de personas buscando por el nombre de la comuna")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personas encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al conectar con el servicio de comunas")
    })
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorComuna(@RequestParam("nombre") String nombre){
        try{
            return ResponseEntity.ok(personaService.findByComunaNombre(nombre));
        }catch(Exception ex){
            ex.printStackTrace(); // ← agrega esto
            return ResponseEntity.status(500).build(); // ← cambia a 500 con mensaje
        }
    }

    //buscar por id de comuna
    @GetMapping("/comunaID/{id}")
    @Operation(summary = "Buscar personas por ID de Comuna", description = "Obtiene una lista de personas buscando por el identificador de la comuna")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personas encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PersonaDTO.Response>> buscarPorComunaID(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(personaService.findByComunaID(id));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).build(); 
        }
    }


    
    
    

}
