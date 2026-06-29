package com.example.edicion.controller;

// === IMPORTS ESTÁTICOS PARA MOCKITO Y MOCKMVC ===
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
// Si usas Spring Boot 3.4+, recuerda cambiar @MockBean por @MockitoBean
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.edicion.assemblers.EdicionModelAssembler;
import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.service.EdicionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EdicionControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class EdicionControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Finge el comportamiento de la base de datos/lógica
    @MockBean
    private EdicionService edicionService;

    // Espía el Assembler real para que pueda construir los Links (HATEOAS)
    @SpyBean
    private EdicionModelAssembler assembler;

    private EdicionDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        // Inicializamos una respuesta base para usarla en los tests
        responseDTO = new EdicionDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setNombre("Edición de Lujo V2");
        responseDTO.setAnnioPublicacion(2023);
    }

    @Test
    @DisplayName("GET Buscar por ID V2: Retorna EntityModel HAL JSON con hipervínculos")
    public void buscarPorId_RetornaModeloHateoas() throws Exception {
        // DADO
        when(edicionService.obtenerPorId(1L)).thenReturn(responseDTO);

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v2/ediciones/{id}", 1L)
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Edición de Lujo V2"))
                // Verificamos que el HATEOAS construyó el link correctamente
                .andExpect(jsonPath("$._links.self.href").exists()); 
    }

    @Test
    @DisplayName("POST Crear V2: Retorna EntityModel HAL JSON con recurso estructurado")
    public void guardar_RetornaModeloHateoas() throws Exception {
        // DADO
        EdicionDTO.Request requestDTO = new EdicionDTO.Request();
        requestDTO.setNombre("Edición de Lujo V2");
        requestDTO.setAnnioPublicacion(2023);

        when(edicionService.guardar(any(EdicionDTO.Request.class))).thenReturn(responseDTO);

        // CUANDO y ENTONCES
        mockMvc.perform(post("/api/v2/ediciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("PUT Actualizar V2: Retorna EntityModel HAL JSON con datos modificados")
    public void actualizar_RetornaModeloHateoas() throws Exception {
        // DADO
        EdicionDTO.Request requestDTO = new EdicionDTO.Request();
        requestDTO.setNombre("Edición de Lujo V2 Modificada");
        requestDTO.setAnnioPublicacion(2024);

        responseDTO.setNombre("Edición de Lujo V2 Modificada");
        responseDTO.setAnnioPublicacion(2024);

        when(edicionService.actualizar(eq(1L), any(EdicionDTO.Request.class))).thenReturn(responseDTO);

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v2/ediciones/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Edición de Lujo V2 Modificada"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("DELETE Eliminar V2: Retorna 204 No Content ante eliminación en HATEOAS")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        // DADO
        doNothing().when(edicionService).eliminar(1L);
        
        // CUANDO y ENTONCES
        mockMvc.perform(delete("/api/v2/ediciones/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}