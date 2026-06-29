package com.biblioteca.editorial.controller;

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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;


import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.editorial.assemblers.EditorialModelAssembler;
import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.service.EditorialService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EditorialControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class EditorialControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EditorialService editorialService;

    // El SpyBean es CRÍTICO para que HATEOAS genere los _links en los tests
    @SpyBean
    private EditorialModelAssembler assembler;

    private EditorialDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new EditorialDTO.Response(1L, "Salamandra");
    }

    @Test
    @DisplayName("GET Listar V2: Retorna Colección HAL JSON con hipervínculos")
    public void listar_RetornaColeccionHateoas() throws Exception {
        when(editorialService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v2/editoriales")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.responseList[0].id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST Crear V2: Retorna EntityModel HAL JSON")
    public void guardar_RetornaModeloHateoas() throws Exception {
        EditorialDTO.Request requestDTO = new EditorialDTO.Request("Salamandra");
        when(editorialService.save(any(EditorialDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v2/editoriales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("PUT Actualizar V2: Retorna EntityModel HAL JSON con datos actualizados")
    public void actualizar_RetornaModeloHateoas() throws Exception {
        EditorialDTO.Request requestDTO = new EditorialDTO.Request("Salamandra Editores");
        responseDTO.setNombre("Salamandra Editores");

        when(editorialService.update(eq(1L), any(EditorialDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v2/editoriales/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Salamandra Editores"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("DELETE Eliminar V2: Retorna 204 No Content")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        doNothing().when(editorialService).delete(1L);

        mockMvc.perform(delete("/api/v2/editoriales/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}