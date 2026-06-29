package com.biblioteca.editorial.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
// Si usas Spring Boot 3.4+, cambia la línea de arriba por:
// import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.service.EditorialService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EditorialController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EditorialControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Recuerda usar @MockitoBean si tu IDE te lo pide por la versión
    @MockBean
    private EditorialService editorialService;

    private EditorialDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new EditorialDTO.Response(1L, "Anagrama");
    }

    @Test
    @DisplayName("GET Listar V1: Retorna 200 OK con la lista de editoriales")
    public void listar_Retorna200YLista() throws Exception {
        when(editorialService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/editoriales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Anagrama"));
    }

    @Test
    @DisplayName("GET Buscar Por ID V1: Retorna 200 OK")
    public void buscarPorId_CuandoExiste_Retorna200() throws Exception {
        when(editorialService.findByIdOrThrow(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/editoriales/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("POST Crear V1: Retorna 201 Created al registrar")
    public void guardar_ConDatosValidos_Retorna201() throws Exception {
        EditorialDTO.Request requestDTO = new EditorialDTO.Request("Anagrama");
        when(editorialService.save(any(EditorialDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/editoriales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT Actualizar V1: Retorna 200 OK con la editorial modificada")
    public void actualizar_CuandoExiste_Retorna200() throws Exception {
        EditorialDTO.Request requestDTO = new EditorialDTO.Request("Anagrama Editores");
        responseDTO.setNombre("Anagrama Editores");
        
        when(editorialService.update(eq(1L), any(EditorialDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/editoriales/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Anagrama Editores"));
    }

    @Test
    @DisplayName("DELETE Eliminar V1: Retorna 204 No Content ante eliminación exitosa")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        doNothing().when(editorialService).delete(1L);

        mockMvc.perform(delete("/api/v1/editoriales/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}