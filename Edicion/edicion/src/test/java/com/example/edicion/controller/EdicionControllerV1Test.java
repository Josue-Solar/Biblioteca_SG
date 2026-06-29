package com.example.edicion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.service.EdicionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EdicionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EdicionControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EdicionService edicionService;

    private EdicionDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new EdicionDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setNombre("Edición Especial");
        responseDTO.setAnnioPublicacion(2021);
    }

    @Test
    @DisplayName("GET Listar V1: Retorna 200 OK con la lista de ediciones")
    public void obtenerTodos_Retorna200YLista() throws Exception {
        // DADO
        when(edicionService.obtenerTodos()).thenReturn(List.of(responseDTO));

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/ediciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Edición Especial"));
    }

    @Test
    @DisplayName("GET Buscar Por ID V1: Retorna 200 OK con la edición correspondiente")
    public void obtenerPorId_CuandoExiste_Retorna200() throws Exception {
        // DADO
        when(edicionService.obtenerPorId(1L)).thenReturn(responseDTO);

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/ediciones/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Edición Especial"));
    }

    @Test
    @DisplayName("POST Crear V1: Retorna 201 Created al registrar una edición válida")
    public void crear_ConDatosValidos_Retorna201() throws Exception {
        // DADO
        EdicionDTO.Request requestDTO = new EdicionDTO.Request();
        requestDTO.setNombre("Edición Especial");
        requestDTO.setAnnioPublicacion(2021);
        
        when(edicionService.guardar(any(EdicionDTO.Request.class))).thenReturn(responseDTO);

        // CUANDO y ENTONCES
        mockMvc.perform(post("/api/v1/ediciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT Actualizar V1: Retorna 200 OK con la edición modificada")
    public void actualizar_CuandoExiste_Retorna200() throws Exception {
        // DADO
        EdicionDTO.Request requestDTO = new EdicionDTO.Request();
        requestDTO.setNombre("Edición Modificada");
        requestDTO.setAnnioPublicacion(2022);
        
        responseDTO.setNombre("Edición Modificada");
        
        when(edicionService.actualizar(eq(1L), any(EdicionDTO.Request.class))).thenReturn(responseDTO);

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v1/ediciones/editar:{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Edición Modificada"));
    }

    @Test
    @DisplayName("DELETE Eliminar (Camino Feliz): Retorna 204 No Content")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        
        // 1. DADO: Simulamos el servicio (como es void, usamos doNothing)
        doNothing().when(edicionService).eliminar(1L);

        // 2. CUANDO y ENTONCES: Ejecutamos el endpoint y esperamos el código HTTP 204
        mockMvc.perform(delete("/api/v1/ediciones/eliminar:{id}", 1L))
                .andExpect(status().isNoContent());

        // 3. Verificamos que el método eliminar se haya llamado exactamente 1 vez con el ID 1
        verify(edicionService, times(1)).eliminar(1L);
    }
}