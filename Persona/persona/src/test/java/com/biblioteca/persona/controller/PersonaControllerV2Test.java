package com.biblioteca.persona.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.biblioteca.persona.assemblers.PersonaModelAssembler;
import com.biblioteca.persona.dto.ComunaDTO;
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.SexoService;
import com.biblioteca.persona.service.impl.PersonaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PersonaControllerV2Test {
    //no se trabajo con spring y solo con mockito(requiere object mapper) q funciona solo
    //lo mas probable por alguna inconsistencia del pom
    // Instanciados manualmente como en tu V1
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    // Usamos @Mock de Mockito puro en lugar de @MockBean
    @Mock
    private PersonaServiceImpl personaService;

    @Mock
    private SexoService sexoService;

    @Mock
    private RolService rolService;

    @Mock
    private PersonaModelAssembler assembler;

    // Inyectamos los mocks en el controlador V2
    @InjectMocks
    private PersonaControllerV2 personaControllerV2;

    private PersonaDTO.Response personaResponsePrueba;
    private PersonaDTO.Request personaRequestPrueba;

    @BeforeEach
    void setUp() {
        // 1. Abrimos los Mocks
        MockitoAnnotations.openMocks(this);
        
        // 2. Construimos el Postman virtual standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(personaControllerV2).build();
        
        // 3. Instanciamos el traductor JSON
        this.objectMapper = new ObjectMapper();

        // 4. Preparamos los datos
        ComunaDTO comuna = new ComunaDTO();
        comuna.setId(1L);
        comuna.setNombre("Santiago");

        SexoDTO.Response sexo = new SexoDTO.Response("Femenino");
        RolDTO.Response rol = new RolDTO.Response("Admin");

        personaResponsePrueba = new PersonaDTO.Response();
        personaResponsePrueba.setId(1L);
        personaResponsePrueba.setNombreCompleto("Ada Lovelace");
        personaResponsePrueba.setRut("11222333-4");
        personaResponsePrueba.setCorreo("ada@biblioteca.com");
        personaResponsePrueba.setComuna(comuna);
        personaResponsePrueba.setSexo(sexo);
        personaResponsePrueba.setRol(rol);

        personaRequestPrueba = new PersonaDTO.Request();
        personaRequestPrueba.setRun("11222333");
        personaRequestPrueba.setDvRun("4");
        personaRequestPrueba.setPNombre("Ada");
        personaRequestPrueba.setApPaterno("Lovelace");
        personaRequestPrueba.setCorreo("ada@biblioteca.com");
        personaRequestPrueba.setComunaId(1L);
        personaRequestPrueba.setSexoId(1L);
        personaRequestPrueba.setIdRol(1L);

        // 5. Truco del Assembler
        EntityModel<PersonaDTO.Response> entityModelPrueba = EntityModel.of(personaResponsePrueba);
        when(assembler.toModel(any())).thenReturn(entityModelPrueba);
    }

    //get all
    @Test
    @DisplayName("GET /api/v2/personas - Camino Feliz: Retorna 200 OK y la lista con datos")
    public void listarTodos_Retorna200YLista() throws Exception {
        // DADO (Given)
        // Simulamos que el servicio devuelve una lista con nuestro objeto de prueba
        // NOTA: Si tu método en el servicio no se llama "findAll()", cámbialo aquí (ej. obtenerTodos())
        when(personaService.findAll()).thenReturn(List.of(personaResponsePrueba));

        // CUANDO y ENTONCES (When & Then)
        mockMvc.perform(get("/api/v2/personas") 
                .accept("application/hal+json")) // Aceptamos formato HATEOAS
                .andExpect(status().isOk());

        // Verificamos que el servicio fue llamado exactamente 1 vez
        verify(personaService, times(1)).findAll();
        
        // Verificamos que el traductor HATEOAS (assembler) procesó nuestro objeto de prueba
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("GET /api/v2/personas - Camino Vacío: Retorna 200 OK y lista vacía")
    public void listarTodos_Retorna200YListaVacia() throws Exception {
        // DADO (Given)
        // Simulamos que la base de datos está vacía devolviendo una lista vacía
        when(personaService.findAll()).thenReturn(Collections.emptyList());

        // CUANDO y ENTONCES (When & Then)
        mockMvc.perform(get("/api/v2/personas")
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        // Verificamos que el servicio fue llamado
        verify(personaService, times(1)).findAll();
        
        // CRÍTICO: Como la lista está vacía, el assembler HATEOAS NO debería haber sido llamado nunca
        verify(assembler, never()).toModel(any());
    }

    // ==========================================
    // 1. BUSCAR POR ID (GET /id)
    // ==========================================

    @Test
    @DisplayName("GET /api/v2/personas/{id} - Éxito: Retorna 200 OK y la persona con HATEOAS")
    public void buscarPorId_Retorna200YPersona() throws Exception {
        // DADO: El servicio encuentra a la persona
        when(personaService.findById(1L)).thenReturn(personaResponsePrueba);

        // CUANDO Y ENTONCES
        mockMvc.perform(get("/api/v2/personas/{id}", 1L)
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).findById(1L);
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("GET /api/v2/personas/{id} - Error: Lanza Excepción si no existe")
    public void buscarPorId_PersonaNoExiste_LanzaExcepcion() throws Exception {
        when(personaService.findById(2L)).thenThrow(new RuntimeException("Persona no encontrada"));

        try {
            mockMvc.perform(get("/api/v2/personas/{id}", 2L)
                    .accept("application/hal+json"));
            org.junit.jupiter.api.Assertions.fail("Se esperaba excepción");
        } catch (Exception e) {
            // Éxito, atrapó el error
        }

        verify(personaService, times(1)).findById(2L);
        verify(assembler, never()).toModel(any());
    }

    // ==========================================
    // 2. ACTUALIZAR POR ID (PUT /id)
    // ==========================================

    @Test
    @DisplayName("PUT /api/v2/personas/{id} - Éxito: Retorna 200 OK y los datos actualizados")
    public void actualizar_Retorna200YPersonaActualizada() throws Exception {
        // DADO: El servicio actualiza correctamente usando el request
        when(personaService.updatePersona(eq(1L), any(PersonaDTO.Request.class))).thenReturn(personaResponsePrueba);

        // CUANDO Y ENTONCES
        mockMvc.perform(put("/api/v2/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personaRequestPrueba))
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).updatePersona(eq(1L), any(PersonaDTO.Request.class));
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("PUT /api/v2/personas/{id} - Error: Retorna 400 si el Request es inválido")
    public void actualizar_RequestInvalido_Retorna400() throws Exception {
        // DADO: Creamos un request malo sin RUN ni Correo para romper las validaciones (@NotBlank, etc.)
        PersonaDTO.Request requestInvalido = new PersonaDTO.Request(); 

        // CUANDO Y ENTONCES
        mockMvc.perform(put("/api/v2/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
                .accept("application/hal+json"))
                .andExpect(status().isBadRequest()); // Valida que los @Valid del controlador funcionen

        // CRÍTICO: Como falló la validación de Spring de entrada, el servicio NUNCA se ejecuta
        verify(personaService, never()).updatePersona(any(), any());
    }

    // ==========================================
    // 3. ELIMINAR POR ID (DELETE /id)
    // ==========================================

    @Test
    @DisplayName("DELETE /api/v2/personas/{id} - Éxito: Retorna 204 No Content")
    public void eliminar_Retorna204NoContent() throws Exception {
        // DADO: El servicio elimina con éxito (los métodos void en Mockito no necesitan "when", no hacen nada por defecto)
        
        // CUANDO Y ENTONCES
        mockMvc.perform(delete("/api/v2/personas/{id}", 1L))
                .andExpect(status().isNoContent()); // O .isOk() si tu controlador devuelve 200 al borrar

        verify(personaService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/v2/personas/{id} - Error: Lanza Excepción al borrar inexistente")
    public void eliminar_PersonaNoExiste_LanzaExcepcion() throws Exception {
        doThrow(new RuntimeException("No existe la persona")).when(personaService).delete(2L);

        try {
            mockMvc.perform(delete("/api/v2/personas/{id}", 2L));
            org.junit.jupiter.api.Assertions.fail("Se esperaba excepción");
        } catch (Exception e) {
            // Éxito, atrapó el error
        }

        verify(personaService, times(1)).delete(2L);
    }

    // ==========================================
    // BUSCAR POR RUN (GET /run/{run})
    // ==========================================

    @Test
    @DisplayName("GET /api/v2/personas/run/{run} - Éxito: Retorna 200 OK y la persona")
    public void buscarPorRun_Retorna200YPersona() throws Exception {
        when(personaService.findByRun("11222333")).thenReturn(personaResponsePrueba);

        mockMvc.perform(get("/api/v2/personas/run/{run}", "11222333")
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).findByRun("11222333");
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("GET /api/v2/personas/run/{run} - Error: Lanza Excepción si RUN no existe")
    public void buscarPorRun_NoExiste_LanzaExcepcion() throws Exception {
        when(personaService.findByRun("99999999")).thenThrow(new RuntimeException("RUN no encontrado"));

        try {
            mockMvc.perform(get("/api/v2/personas/run/{run}", "99999999")
                    .accept("application/hal+json"));
            org.junit.jupiter.api.Assertions.fail("Se esperaba excepción");
        } catch (Exception e) {
            // Éxito, atrapó el error
        }

        verify(personaService, times(1)).findByRun("99999999");
    }

    // ==========================================
    //BUSCAR POR APELLIDO
    // ==========================================

    @Test
    @DisplayName("GET /api/v2/personas/apellido/{apellido} - Éxito: Retorna 200 OK y lista de personas")
    public void buscarPorApellido_Retorna200YLista() throws Exception {
        // CAMINO BUENO: Encuentra coincidencias
        when(personaService.findByApPaterno("Lovelace")).thenReturn(List.of(personaResponsePrueba));

        mockMvc.perform(get("/api/v2/personas/apellido/{apellido}", "Lovelace")
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).findByApPaterno("Lovelace");
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("GET /api/v2/personas/apellido/{apellido} - Error: Lanza Excepción")
    public void buscarPorApellido_NoExiste_LanzaExcepcion() throws Exception {
        when(personaService.findByApPaterno("Inexistente")).thenThrow(new RuntimeException("Apellido no encontrado"));

        try {
            mockMvc.perform(get("/api/v2/personas/apellido/{apellido}", "Inexistente")
                    .accept("application/hal+json"));
            org.junit.jupiter.api.Assertions.fail("Se esperaba excepción");
        } catch (Exception e) {
            // Éxito
        }

        verify(personaService, times(1)).findByApPaterno("Inexistente");
        verify(assembler, never()).toModel(any());
    }

    // ==========================================
    // BUSCAR POR NOMBRE DE COMUNA
    // ==========================================

    @Test
    @DisplayName("GET /api/v2/personas/comuna?nombre={nombre} - Éxito: Retorna 200 OK y lista de personas")
    public void buscarPorNombreComuna_Retorna200YLista() throws Exception {
        // CAMINO BUENO: Encuentra personas en esa comuna
        when(personaService.findByComunaNombre("Santiago")).thenReturn(List.of(personaResponsePrueba));

        // SOLUCIÓN: Usamos .param() en lugar de ponerlo en la URL
        mockMvc.perform(get("/api/v2/personas/comuna")
                .param("nombre", "Santiago")
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).findByComunaNombre("Santiago");
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("GET /api/v2/personas/comuna?nombre={nombre} - Error: Lanza Excepción")
    public void buscarPorNombreComuna_NoExiste_LanzaExcepcion() throws Exception {
        when(personaService.findByComunaNombre("Inexistente")).thenThrow(new RuntimeException("Comuna no encontrada"));

        try {
            mockMvc.perform(get("/api/v2/personas/comuna")
                    .param("nombre", "Inexistente")
                    .accept("application/hal+json"));
            org.junit.jupiter.api.Assertions.fail("Se esperaba excepción");
        } catch (Exception e) {
            // Éxito
        }

        verify(personaService, times(1)).findByComunaNombre("Inexistente");
    }

    // ==========================================
    // BUSCAR POR ROL
    // ==========================================

    @Test
    @DisplayName("GET /api/v2/personas/rol/{rolId} - Éxito: Retorna 200 OK y lista de personas")
    public void buscarPorRol_Retorna200YLista() throws Exception {
        // DADO: Primero simulamos el RolService para que devuelva un rol válido, luego el PersonaService
        RolDTO.Response rolSimulado = new RolDTO.Response("Admin");
        when(rolService.findByIdOrThrow(1L)).thenReturn(rolSimulado);
        when(personaService.findByRol(rolSimulado)).thenReturn(List.of(personaResponsePrueba));

        // CUANDO Y ENTONCES: Pasamos 1L como parámetro, NO una palabra
        mockMvc.perform(get("/api/v2/personas/rol/{rolId}", 1L)
                .accept("application/hal+json"))
                .andExpect(status().isOk());

        verify(rolService, times(1)).findByIdOrThrow(1L);
        verify(personaService, times(1)).findByRol(rolSimulado);
        verify(assembler, times(1)).toModel(personaResponsePrueba);
    }

    @Test
    @DisplayName("GET /api/v2/personas/rol/{rolId} - Error: Lanza Excepción")
    public void buscarPorRol_NoExiste_LanzaExcepcion() throws Exception {
        // DADO: El RolService explota al buscar un ID que no existe
        when(rolService.findByIdOrThrow(99L)).thenThrow(new RuntimeException("Rol no encontrado"));

        try {
            // Pasamos 99L, no la palabra "Inexistente"
            mockMvc.perform(get("/api/v2/personas/rol/{rolId}", 99L)
                    .accept("application/hal+json"));
            org.junit.jupiter.api.Assertions.fail("Se esperaba excepción");
        } catch (Exception e) {
            // Éxito
        }

        verify(rolService, times(1)).findByIdOrThrow(99L);
        verify(personaService, never()).findByRol(any()); // Nunca llega a buscar personas
    }
}
